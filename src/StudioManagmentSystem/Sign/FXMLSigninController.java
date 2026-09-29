package StudioManagmentSystem.Sign;

import StudioManagmentSystem.UserDAO;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FXMLSigninController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;
    
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField ageField;

    @FXML
    private Button submitButton;

    @FXML
    private Button clearButton;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleSignUpButton(ActionEvent event) {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String age = ageField.getText();
        String username = usernameField.getText();
        String passcode = passwordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || age.isEmpty() || username.isEmpty() || passcode.isEmpty()) {
            System.out.println("Please fill in all fields.");
            return;
        }
        if (!passcode.equals(confirmPasswordField.getText())) {
            System.out.println("Passwords do not match.");
            return;
        }

        // Test the database connection
        userDAO.test_conn();

        // Create the user
        boolean success = userDAO.CreateUser(firstName, lastName, age, username, passcode);

        if (success) {
            handleSubmit(event);
        } else {
            System.out.println("User creation failed.");
        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StudioManagmentSystem/Login/FXMLLogin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) submitButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the login screen.");
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        firstNameField.clear();
        lastNameField.clear();
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        ageField.clear();
    }
}
