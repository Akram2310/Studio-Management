package StudioManagmentSystem;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class UserDAO {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public void test_conn() {
        try (Connection con = DatabaseConnector.getConnection()) {
            System.out.println("Database connection succeeded");
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // ---------- Password hashing (PBKDF2 with a random salt, stored as salt:hash) ----------

    private static String hashPassword(String password) {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(pbkdf2(password, salt));
    }

    private static boolean verifyPassword(String password, String stored) {
        if (stored == null || !stored.contains(":")) {
            return false;
        }
        String[] parts = stored.split(":", 2);
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] expected = Base64.getDecoder().decode(parts[1]);
        byte[] actual = pbkdf2(password, salt);
        return java.security.MessageDigest.isEqual(expected, actual);
    }

    private static byte[] pbkdf2(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Password hashing failed", e);
        }
    }

    // ---------- Equipment ----------

    public boolean addEquipment(String equipmentId, String equipmentName, int quantity) {
        String query = "INSERT INTO equipment (equipment_id, equipment_name, quantity) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, equipmentId);
            statement.setString(2, equipmentName);
            statement.setInt(3, quantity);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error occurred while adding equipment: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEquipmentById(String equipmentId) {
        String query = "DELETE FROM equipment WHERE equipment_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, equipmentId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error occurred while deleting equipment: " + e.getMessage());
            return false;
        }
    }

    public boolean checkEquipmentExists(String equipmentId) {
        String query = "SELECT equipment_id FROM equipment WHERE equipment_id = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, equipmentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while checking equipment: " + e.getMessage());
            return false;
        }
    }

    // ---------- Users ----------

    public boolean CreateUser(String firstName, String lastName, String age, String username, String passcode) {
        String query = "INSERT INTO userPersonalInformation (fname, lname, age, username, password) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, age);
            statement.setString(4, username);
            statement.setString(5, hashPassword(passcode));
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error occurred while creating user: " + e.getMessage());
            return false;
        }
    }

    public boolean checkAlreadyAUser(String username, String passcode) {
        String query = "SELECT password FROM userPersonalInformation WHERE username = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && verifyPassword(passcode, resultSet.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while checking user: " + e.getMessage());
            return false;
        }
    }

    public int counter_of_users() {
        String query = "SELECT COUNT(*) AS user_count FROM userPersonalInformation";
        try (Connection con = DatabaseConnector.getConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("user_count");
            }
        } catch (SQLException e) {
            System.out.println("Error occurred while counting users: " + e.getMessage());
        }
        return 0;
    }
}
