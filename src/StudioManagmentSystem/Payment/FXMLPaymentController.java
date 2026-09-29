package StudioManagmentSystem.Payment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.LocalDate;

public class FXMLPaymentController {

    @FXML
    private TextField cardHolderNameField;

    @FXML
    private PasswordField cardNumberField;

    @FXML
    private DatePicker expDatePicker;

    @FXML
    private PasswordField cvvField;

    @FXML
    private TextField totalField;

    @FXML
    private Button payButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        payButton.setOnAction(this::handlePayment);
        totalField.setText("1000");
        totalField.setEditable(false);
    }

    @FXML
    private void handlePayment(ActionEvent event) {
        String cardHolderName = cardHolderNameField.getText();
        String cardNumber = cardNumberField.getText();
        LocalDate expDate = expDatePicker.getValue();
        String cvv = cvvField.getText();
        String total = totalField.getText();

       if (cardHolderName.isEmpty() || cardNumber.isEmpty() || expDate == null || cvv.isEmpty()) {
            showAlert(AlertType.ERROR, "Payment Failed", "Please enter all details");
        } else if (cardNumber.length() < 16) {
            showAlert(AlertType.ERROR, "Payment Failed", "Card number must be at least 16 digits");
        } else if (cvv.length() < 3) {
            showAlert(AlertType.ERROR, "Payment Failed", "CVV number must be at least 3 digits");
        } else {
            // Logic to handle payment (e.g., validate card details, process payment)
            showAlert(AlertType.INFORMATION, "Payment Successful", "Payment of " + total + " has been made successfully!");
        }
    }
    

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
