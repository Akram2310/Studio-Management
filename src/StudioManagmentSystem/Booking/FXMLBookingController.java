package StudioManagmentSystem.Booking;

import StudioManagmentSystem.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FXMLBookingController {
    @FXML
    private TextField dateField;
    @FXML
    private TextField customerNameField;
    @FXML
    private ListView<String> bookingListView;

    @FXML
    private void handleBook() {
        String customerName = customerNameField.getText();
        LocalDate date = parseDate(dateField.getText());

        if (customerName == null || customerName.trim().isEmpty() || date == null) {
            showAlert(AlertType.ERROR, "Booking Failed", "Please enter all details");
            return;
        }

        // Save to database
        if (saveBookingToDatabase(customerName, date)) {
            String booking = "Customer: " + customerName + " | Date: " + date.toString();
            if (bookingListView != null) {
                bookingListView.getItems().add(booking);
            }
            customerNameField.clear();
            dateField.clear();
            showAlert(AlertType.INFORMATION, "Booking Added", "Booking for " + customerName + " on " + date.toString() + " added.");
            switchToPaymentScreen();
        } else {
            showAlert(AlertType.ERROR, "Database Error", "Failed to save booking to database.");
        }
    }

    private boolean saveBookingToDatabase(String customerName, LocalDate date) {
        String sql = "INSERT INTO bookings (customer_name, booking_date) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, customerName);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private LocalDate parseDate(String dateText) {
        if (dateText == null || dateText.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateText);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void switchToPaymentScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudioManagmentSystem/Payment/FXMLPayment.fxml"));
            Parent paymentScreen = loader.load();
            Stage stage = (Stage) customerNameField.getScene().getWindow();
            stage.setScene(new Scene(paymentScreen));
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Error", "Failed to load the payment screen.");
            e.printStackTrace();
        }
    }
}