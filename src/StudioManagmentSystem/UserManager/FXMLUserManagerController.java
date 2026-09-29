package StudioManagmentSystem.UserManager;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLUserManagerController {

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField jobTypeField;

    @FXML
    private Button createButton;

    @FXML
    private void initialize() {
        // You can initialize any data or bind data here if needed
    }

    @FXML
    private void handleCreate() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String jobType = jobTypeField.getText();

        if (name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || jobType.isEmpty()) {
            showAlert(AlertType.ERROR, "Creation Failed", "Please enter all details");
        } else if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Creation Failed", "Passwords do not match");
        } else {
            // Logic to handle user creation (e.g., save user details to database)
            showAlert(AlertType.INFORMATION, "User Created", "User " + name + " has been created successfully!");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleToolsAction(ActionEvent event) {
        try {
            // Load the Equipment Management FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudioManagmentSystem/Equipmanage/FXMLEquipmanage.fxml"));
            Parent equipmentRoot = loader.load();

            // Create a new stage (window) to show the equipment screen
            Stage stage = new Stage();
            stage.setTitle("Equipment Management");
            stage.setScene(new Scene(equipmentRoot));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
}
