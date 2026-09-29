package StudioManagmentSystem.Login;

import StudioManagmentSystem.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class FXMLLoginController implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button SignupButton;

    private UserDAO userDAO = new UserDAO();

    public void initialize(URL url, ResourceBundle rb) {
        // Initialize
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        userDAO.test_conn();
        boolean loginSuccess = userDAO.checkAlreadyAUser(username, password);

        if (loginSuccess) {
            System.out.println("Login successful");
            LoginAccess();
        } else {
            System.out.println("Login failed");
            showAlert("Login Error", "Incorrect username or password. Please try again.");
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            // Load the Sign Up screen
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/StudioManagmentSystem/Sign/FXMLSignin.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sign Up Screen");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading Sign Up screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAdmin(ActionEvent event) {
        try {
            // Load the UserManager screen
            Node node = (Node) event.getSource();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/StudioManagmentSystem/UserManager/FXMLUserManager.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("User Manager");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading User Manager screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void LoginAccess() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/StudioManagmentSystem/Booking/FXMLBooking.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException exception) {
            System.out.println("Error loading new scene: " + exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
