package org.group1.asda;

import javafx.application.Application;
import javafx.stage.Stage;
import org.group1.asda.navigation.Router;
import org.group1.asda.persistence.Database;

public class TestHome extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Database.init(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Router router = new Router(stage);
        router.goTo("home");
        stage.setTitle("ASDA - Home Test");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
