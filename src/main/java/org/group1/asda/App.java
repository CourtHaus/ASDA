package org.group1.asda;

import javafx.application.Application;
import javafx.stage.Stage;
import org.group1.asda.assessment.AssessmentCli;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.Database;

import java.util.Arrays;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        // Initialize database (creates schema and seeds questions on first run)
        Database.init();

        Router router = new Router(stage);
        router.goTo("home");
        stage.setTitle("ASDA");
        stage.show();
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
