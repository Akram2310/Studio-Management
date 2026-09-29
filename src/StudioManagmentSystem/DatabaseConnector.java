package StudioManagmentSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Creates MySQL connections. Credentials are NOT stored in code.
 * They are read from (in order): environment variables STUDIO_DB_URL /
 * STUDIO_DB_USER / STUDIO_DB_PASSWORD, then a local config.properties file
 * in the working directory (see config.example.properties).
 */
public class DatabaseConnector {

    private static final Properties CONFIG = loadConfig();

    private static Properties loadConfig() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("config.properties")) {
            props.load(in);
        } catch (IOException e) {
            // No config file: fall back to environment variables only.
        }
        return props;
    }

    private static String setting(String envName, String propName, String defaultValue) {
        String value = System.getenv(envName);
        if (value == null || value.isEmpty()) {
            value = CONFIG.getProperty(propName, defaultValue);
        }
        return value;
    }

    public static Connection getConnection() throws SQLException {
        String url = setting("STUDIO_DB_URL", "db.url", "jdbc:mysql://localhost:3306/studio");
        String user = setting("STUDIO_DB_USER", "db.user", "studio");
        String password = setting("STUDIO_DB_PASSWORD", "db.password", "");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found. Add mysql-connector-j to the project libraries.", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
}
