package org.group1.asda;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.group1.asda.assessment.AssessmentCli;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.Database;
import org.group1.asda.ui.loading.LoadingController;
import org.group1.asda.ui.disclosure.DisclosureController;

import java.io.InputStream;
import java.util.Arrays;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        // Load bundled fonts early (Roboto). Missing files are ignored gracefully.
        loadFont("/fonts/Roboto-Light.ttf");
        loadFont("/fonts/Roboto-Regular.ttf");
        loadFont("/fonts/Roboto-Medium.ttf");
        loadFont("/fonts/Roboto-Bold.ttf");

        // Read minimum loading screen duration (ms) from system property with default 2400 ms
        long minDurationMs = parseLongProp("asda.loading.minMs", 2400);

        // Create the router and immediately show the loading screen, capturing the controller
        Router router = new Router(stage);
        LoadingController loadingController = router.goToAndGetController("loading", LoadingController.class);
        if (loadingController != null) {
            loadingController.setProgress(0.0);
        }
        stage.setTitle("ASDA");
        stage.show();

        // Mark when the loading screen became visible
        final long shownAtNanos = System.nanoTime();

        // Perform initialization on a background thread, update progress, then navigate to home
        Thread initThread = new Thread(() -> {
            try {
                Database.init(p -> Platform.runLater(() -> {
                    if (loadingController != null) {
                        loadingController.setProgress(p);
                    }
                }));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(() -> {
                    if (loadingController != null) {
                        loadingController.setProgress(1.0);
                    }
                    // Enforce minimum display duration
                    long elapsedMs = (System.nanoTime() - shownAtNanos) / 1_000_000L;
                    long remainingMs = Math.max(0L, minDurationMs - elapsedMs);
                    if (remainingMs > 0L) {
                        PauseTransition pause = new PauseTransition(Duration.millis(remainingMs));
                        pause.setOnFinished(ev -> {
                            String next = DisclosureController.hasUserConsented() ? "home" : "disclosure";
                            router.goTo(next);
                        });
                        pause.play();
                    } else {
                        String next = DisclosureController.hasUserConsented() ? "home" : "disclosure";
                        router.goTo(next);
                    }
                });
            }
        }, "app-init-thread");
        initThread.setDaemon(true);
        initThread.start();
    }

    private static void loadFont(String resourcePath) {
        try (InputStream is = App.class.getResourceAsStream(resourcePath)) {
            if (is != null) {
                Font.loadFont(is, 12);
            } else {
                System.out.println("[DEBUG_LOG] Font not found: " + resourcePath);
            }
        } catch (Exception e) {
            System.out.println("[DEBUG_LOG] Failed to load font: " + resourcePath + ": " + e.getMessage());
        }
    }

    private static long parseLongProp(String key, long defaultValue) {
        String v = System.getProperty(key);
        if (v == null || v.isBlank()) return defaultValue;
        try {
            return Long.parseLong(v.trim());
        } catch (NumberFormatException e) {
            System.out.println("[DEBUG_LOG] Invalid long value for -D" + key + "='" + v + "', using default=" + defaultValue);
            return defaultValue;
        }
    }

    public static void main(String[] args) {
        // If launched with --cli, run the terminal AQ assessment and exit.
        boolean cli = Arrays.stream(args).anyMatch("--cli"::equals);
        if (cli) {
            int exit = AssessmentCli.run();
            System.exit(exit);
            return;
        }
        launch(args);
    }
}
