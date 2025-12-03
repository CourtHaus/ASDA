package org.group1.asda.ui.matchinggametutorial;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.group1.asda.navigation.Router;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MatchingGameTutorialController implements Initializable {

    // Pages
    @FXML private VBox pageWelcome;
    @FXML private VBox pageCards;
    @FXML private VBox pageFlip;
    @FXML private javafx.scene.control.ScrollPane pageMatches;
    @FXML private VBox pageHappens;
    @FXML private VBox pageAllSet;

    // Container
    @FXML private javafx.scene.layout.StackPane pageContainer;

    // Footer controls
    @FXML private Button backButton;
    @FXML private Button btnPrev;
    @FXML private Button btnNext;
    @FXML private Button nextButton;
    @FXML private Button beginButton;
    @FXML private Button btnSkip;
    @FXML private HBox dots;

    // State
    private final List<Node> pages = new ArrayList<>();
    private final List<Circle> indicatorDots = new ArrayList<>();
    private int currentPageIndex = 0;

    private static final int PAGE_WELCOME = 0;
    private static final int PAGE_CARDS = 1;
    private static final int PAGE_FLIP = 2;
    private static final int PAGE_MATCHES = 3;
    private static final int PAGE_HAPPENS = 4;
    private static final int PAGE_ALL_SET = 5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pages.add(pageWelcome);
        pages.add(pageCards);
        pages.add(pageFlip);
        pages.add(pageMatches);
        pages.add(pageHappens);
        pages.add(pageAllSet);

        if (dots != null) {
            for (Node n : dots.getChildren()) {
                if (n instanceof Circle) {
                    indicatorDots.add((Circle) n);
                }
            }
        }

        // Attach handlers
        btnPrev.setOnAction(e -> showPreviousPage());
        backButton.setOnAction(e -> onBack());
        btnNext.setOnAction(e -> showNextPage());
        if (nextButton != null) nextButton.setOnAction(e -> showNextPage());
        if (beginButton != null) beginButton.setOnAction(e -> onBegin());
        if (btnSkip != null) btnSkip.setOnAction(e -> onSkip());

        // Focus handling
        if (pageContainer != null) {
            pageContainer.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null) {
                    PauseTransition pt = new PauseTransition(Duration.millis(120));
                    pt.setOnFinished(ev -> pageContainer.requestFocus());
                    pt.play();
                }
            });
        }

        showPage(0);
    }

    private void showPage(int index) {
        if (index < 0 || index >= pages.size()) return;

        for (int i = 0; i < pages.size(); i++) {
            Node p = pages.get(i);
            if (p != null) {
                p.setVisible(i == index);
                p.setManaged(i == index);
            }
        }

        updateDots(index);

        btnPrev.setDisable(index == 0);
        btnNext.setDisable(index == pages.size() - 1);
        backButton.setDisable(index == 0);

        boolean isLastPage = (index == pages.size() - 1);
        if (nextButton != null) {
            nextButton.setVisible(!isLastPage);
            nextButton.setManaged(!isLastPage);
        }
        if (beginButton != null) {
            beginButton.setVisible(isLastPage);
            beginButton.setManaged(isLastPage);
        }

        currentPageIndex = index;
    }

    private void updateDots(int activeIndex) {
        for (int i = 0; i < indicatorDots.size(); i++) {
            Circle dot = indicatorDots.get(i);
            if (i == activeIndex) {
                dot.setStyle("-fx-fill: #8ba888;");
            } else {
                dot.setStyle("-fx-fill: #c2bfc2;");
            }
        }
    }

    private void showNextPage() {
        if (currentPageIndex < pages.size() - 1) {
            showPage(currentPageIndex + 1);
        }
    }

    private void showPreviousPage() {
        if (currentPageIndex > 0) {
            showPage(currentPageIndex - 1);
        }
    }

    @FXML
    public void onBegin() {
        navigateToGame();
    }

    @FXML
    public void onSkip() {
        navigateToGame();
    }

    @FXML
    public void onNext() {
        showNextPage();
    }

    @FXML
    public void onBack() {
        Router.getInstance().goTo("home");
    }

    private void navigateToGame() {
        Router.getInstance().goTo("matching-game");
    }
}
