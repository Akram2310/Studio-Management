package StudioManagmentSystem.Equipmanage;

import StudioManagmentSystem.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FXMLEquipmanageController {
    @FXML
    private TextField equipmentNameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField equipmentIdField;
    @FXML
    private TextField deleteEquipmentIdField;
    @FXML
    private ListView<String> equipmentListView;

    @FXML
    private void handleAddEquipment() {
        String equipmentId = equipmentIdField.getText();
        String equipmentName = equipmentNameField.getText();
        String quantityStr = quantityField.getText();

        if (equipmentId.isEmpty() || equipmentName.isEmpty() || quantityStr.isEmpty()) {
            showAlert(AlertType.ERROR, "Add Equipment Failed", "Please enter all details");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Add Equipment Failed", "Quantity must be a number");
            return;
        }

        if (addEquipmentToDatabase(equipmentId, equipmentName, quantity)) {
            String equipment = "ID: " + equipmentId + " | Name: " + equipmentName + " | Quantity: " + quantity;
            equipmentListView.getItems().add(equipment);
            equipmentIdField.clear();
            equipmentNameField.clear();
            quantityField.clear();
            showAlert(AlertType.INFORMATION, "Equipment Added", "Equipment " + equipmentName + " added.");
        } else {
            showAlert(AlertType.ERROR, "Add Equipment Failed", "Failed to add equipment to the database.");
        }
    }

    private boolean addEquipmentToDatabase(String equipmentId, String equipmentName, int quantity) {
        String sql = "INSERT INTO equipment (equipment_id, equipment_name, quantity) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, equipmentId);
            pstmt.setString(2, equipmentName);
            pstmt.setInt(3, quantity);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void handleCancel() {
        equipmentNameField.clear();
        quantityField.clear();
        equipmentIdField.clear();
        deleteEquipmentIdField.clear();
    }

    @FXML
    private void handleDeleteEquipment() {
        String equipmentId = deleteEquipmentIdField.getText();
        
        if (equipmentId.isEmpty()) {
            showAlert(AlertType.ERROR, "Delete Equipment Failed", "Please enter the equipment ID");
            return;
        }

        if (checkEquipmentExists(equipmentId)) {
            if (deleteEquipmentFromDatabase(equipmentId)) {
                equipmentListView.getItems().removeIf(item -> item.contains("ID: " + equipmentId));
                showAlert(AlertType.INFORMATION, "Equipment Deleted", "Equipment ID " + equipmentId + " successfully deleted.");
                deleteEquipmentIdField.clear();
            } else {
                showAlert(AlertType.ERROR, "Delete Equipment Failed", "Failed to delete equipment from the database.");
            }
        } else {
            showAlert(AlertType.ERROR, "Delete Equipment Failed", "Equipment ID " + equipmentId + " not found.");
        }
    }

    private boolean checkEquipmentExists(String equipmentId) {
        String sql = "SELECT equipment_id FROM equipment WHERE equipment_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, equipmentId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteEquipmentFromDatabase(String equipmentId) {
        String sql = "DELETE FROM equipment WHERE equipment_id = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, equipmentId);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Optional: Load existing equipment when the controller initializes
    @FXML
    public void initialize() {
        loadExistingEquipment();
    }

    private void loadExistingEquipment() {
        String sql = "SELECT * FROM equipment";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                String equipment = String.format("ID: %s | Name: %s | Quantity: %d",
                    rs.getString("equipment_id"),
                    rs.getString("equipment_name"),
                    rs.getInt("quantity"));
                equipmentListView.getItems().add(equipment);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Load Error", "Failed to load existing equipment.");
        }
    }
}