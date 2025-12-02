package org.group1.asda.ui.disclosure;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.group1.asda.navigation.Router;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

public class DisclosureController {
    @FXML private ScrollPane scrollPane;
    @FXML private VBox contentContainer;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;
    @FXML private Button acceptButton;
    @FXML private Button declineButton;
    @FXML private CheckBox consentCheckBox;

    private boolean hasReadDisclosure = false;
    private static final String CONSENT_KEY = "gameplay.disclosure.accepted";
    private static final String CONSENT_DATE_KEY = "gameplay.disclosure.date";

    @FXML
    public void initialize() {
        // Initially disable accept button until user scrolls through and checks consent
        acceptButton.setDisable(true);

        // Enable accept button only when consent checkbox is checked
        consentCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            acceptButton.setDisable(!isSelected || !hasReadDisclosure);
        });

        // Track if user has scrolled to bottom
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            // Consider "read" if scrolled to at least 95% of content
            if (newVal.doubleValue() >= 0.95) {
                hasReadDisclosure = true;
                updateAcceptButtonState();
            }
        });

        // Set up the scrollpane
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        setupContent();
    }

    private void updateAcceptButtonState() {
        acceptButton.setDisable(!consentCheckBox.isSelected() || !hasReadDisclosure);
    }

    private void setupContent() {
        // This content is populated by the FXML, but we can add dynamic elements here if needed

        // Check if we need to add any dynamic content like timestamps
        // For now, the static content is defined in FXML
    }

    @FXML
    public void onAccept() {
        // Store consent acceptance
        storeConsent();

        // Navigate to home screen
        Router.getInstance().goTo("home");
    }

    @FXML
    public void onDecline() {
        // Show confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Decline Confirmation");
        alert.setHeaderText("Are you sure you want to decline?");
        alert.setContentText("If you decline, you will not be able to use the application. " +
                "You can restart the application later if you change your mind.");

        ButtonType yesButton = new ButtonType("Yes, Exit");
        ButtonType noButton = new ButtonType("No, Go Back", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                // Exit the application
                System.exit(0);
            }
            // Otherwise, do nothing and let user continue reading
        });
    }

    private void storeConsent() {
        // In a real application, this would be stored in a database or preferences
        // For now, we'll store it in system properties (in production, use java.util.prefs.Preferences)
        System.setProperty(CONSENT_KEY, "true");
        System.setProperty(CONSENT_DATE_KEY, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Log consent for audit purposes
        System.out.println("User consent granted at: " + LocalDateTime.now());
    }

    public static boolean hasUserConsented() {
        // Check if user has previously consented
        return "true".equals(System.getProperty(CONSENT_KEY));
    }

    public static void clearConsent() {
        // Method to clear consent (useful for testing or user request)
        System.clearProperty(CONSENT_KEY);
        System.clearProperty(CONSENT_DATE_KEY);
    }
}