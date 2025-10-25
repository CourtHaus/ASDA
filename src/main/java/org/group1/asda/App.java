package org.group1.asda;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.Database;

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
        launch(args);
    }
}
