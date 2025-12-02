// java
package org.group1.asda.ui.attentiongametutorial;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AttentionGameTutorialController implements Initializable {

    // Pages (fx:id set in FXML)
    @FXML private VBox pageWelcome;
    @FXML private VBox pageShapes;
    @FXML private VBox pageColors;
    @FXML private VBox pageMatches;
    @FXML private VBox pagePractice;
    @FXML private VBox pageAllSet;

    // Container holding the pages (this corresponds to the VBox inside ScrollPane in the FXML)
    @FXML private VBox contentStack;

    // Footer controls
    @FXML private Button backButton;
    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private Button beginButton;
    @FXML private Button btnSkip;

    // Practice button (large SPACE)
    @FXML private Button btnPracticeSpace;

    // Dot indicators container (HBox with Circle children)
    @FXML private HBox dots;

    // Internal state
    private final List<VBox> pages = new ArrayList<>();
    private final List<Circle> indicatorDots = new ArrayList<>();
    private int currentPageIndex = 0;

    // Index mapping (makes it clear)
    private static final int PAGE_WELCOME = 0;
    private static final int PAGE_SHAPES = 1;
    private static final int PAGE_COLORS = 2;
    private static final int PAGE_MATCHES = 3;
    private static final int PAGE_PRACTICE = 4;
    private static final int PAGE_ALLSET = 5;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Build pages list in the same order as dots / pagination
        pages.clear();
        pages.add(pageWelcome);
        pages.add(pageShapes);
        pages.add(pageColors);
        pages.add(pageMatches);
        pages.add(pagePractice);
        pages.add(pageAllSet);

        // Collect circles from the dots HBox (safe-guard: only take Circle nodes)
        indicatorDots.clear();
        for (Node n : dots.getChildren()) {
            if (n instanceof Circle) indicatorDots.add((Circle) n);
        }

        // Ensure number of dots matches pages; if not, we will create/adjust visual effect later.
        // Attach handlers
        btnPrev.setOnAction(e -> showPreviousPage());
        backButton.setOnAction(e -> onBack());
        btnNext.setOnAction(e -> showNextPage());
        beginButton.setOnAction(e -> onBegin());
        btnSkip.setOnAction(e -> onSkip());
        btnPracticeSpace.setOnAction(e -> onPracticeSpaceClicked());

        // Keyboard focus handling: if scene becomes available, request focus on the container so onKeyPressed works
        if (contentStack != null) {
            contentStack.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    // set a short delay then request focus; helps when dialog is shown
                    PauseTransition pt = new PauseTransition(Duration.millis(120));
                    pt.setOnFinished(ev -> contentStack.requestFocus());
                    pt.play();
                }
            });
        }

        // Show initial page
        showPage(currentPageIndex);
    }

    // Public handler wired in FXML for key pressed events at root
    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.RIGHT) {
            showNextPage();
            event.consume();
            return;
        }
        if (event.getCode() == KeyCode.LEFT) {
            showPreviousPage();
            event.consume();
            return;
        }
        if (event.getCode() == KeyCode.SPACE) {
            // If on practice page, treat space as a practice press
            if (currentPageIndex == PAGE_PRACTICE) {
                simulatePracticePress();
                event.consume();
            }
        }
    }

    // Called when user clicks the large practice button
    private void onPracticeSpaceClicked() {
        simulatePracticePress();
    }

    // Visual feedback for a practice press: briefly animate the practice button
    private void simulatePracticePress() {
        if (btnPracticeSpace == null) return;

        String originalStyle = btnPracticeSpace.getStyle();
        // simple pressed style
        btnPracticeSpace.setStyle(originalStyle + "-fx-scale-x: 0.98; -fx-scale-y: 0.98; -fx-opacity: 0.9;");
        Timeline t = new Timeline(new KeyFrame(Duration.millis(140), ae -> btnPracticeSpace.setStyle(originalStyle)));
        t.play();
    }

    private void showPreviousPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            showPage(currentPageIndex);
        }
    }

    private void showNextPage() {
        if (currentPageIndex < pages.size() - 1) {
            currentPageIndex++;
            showPage(currentPageIndex);
        }
    }

    // Update visibility of the pages and footer controls / dots
    private void showPage(int index) {
        if (index < 0 || index >= pages.size()) return;

        // set visibility for all pages
        for (int i = 0; i < pages.size(); i++) {
            VBox p = pages.get(i);
            if (p != null) {
                p.setVisible(i == index);
                p.setManaged(i == index);
            }
        }

        // Update dot indicators (fill color)
        updateDots(index);

        // Prev / Next button enabled states
        btnPrev.setDisable(index == 0);
        btnNext.setDisable(index == pages.size() - 1);

        // Back button shown for all but first page (example)
        backButton.setDisable(index == 0);

        // Begin/Start button should only be enabled on last page
        beginButton.setDisable(index != pages.size() - 1);

        // Optionally focus the practice button if on practice page
        if (index == PAGE_PRACTICE && btnPracticeSpace != null) {
            // small delay to allow layout
            PauseTransition pt = new PauseTransition(Duration.millis(80));
            pt.setOnFinished(e -> btnPracticeSpace.requestFocus());
            pt.play();
        }
    }

    private void updateDots(int activeIndex) {
        // If we have Circle nodes, color the active one
        for (int i = 0; i < indicatorDots.size(); i++) {
            Circle c = indicatorDots.get(i);
            if (c == null) continue;
            if (i == activeIndex) {
                // active color used in your CSS: #9ed7ca (teal)
                c.setStyle("-fx-fill: #9ed7ca;");
            } else {
                c.setStyle("-fx-fill: #c2bfc2;"); // default gray used in FXML
            }
        }
    }

    // Back button action - this could close the tutorial or navigate back
    @FXML
    public void onBack() {
        // Default behavior: go to previous page, if first -> no-op
        if (currentPageIndex > 0) {
            showPreviousPage();
        }
    }

    // Begin button action - start the actual game
    @FXML
    public void onBegin() {
        // Real implementation should switch scene or notify parent controller.
        // For now, just log to stdout and disable UI to indicate action.
        System.out.println("Attention tutorial: Begin game requested.");
        // Disable tutorial controls to avoid repeated clicks
        disableAllControls();
    }

    private void disableAllControls() {
        try {
            for (Node n : contentStack.getChildren()) {
                n.setDisable(true);
            }
        } catch (Exception ignored) {}
        backButton.setDisable(true);
        btnPrev.setDisable(true);
        btnNext.setDisable(true);
        beginButton.setDisable(true);
        btnSkip.setDisable(true);
    }

    // Skip button - user wants to leave tutorial
    @FXML
    public void onSkip() {
        System.out.println("Attention tutorial: Skip requested.");
        disableAllControls();
    }
}
