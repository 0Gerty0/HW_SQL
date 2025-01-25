package ru.netology.sqltests;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MySQLTest {
    @Test
    void testMySQLConnection() throws Exception {
        try (MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("test_db")
                .withUsername("user")
                .withPassword("pass")) {
            mysql.start();

            String url = mysql.getJdbcUrl();
            try (Connection conn = DriverManager.getConnection(url, "user", "pass");
                 Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS test (id INT PRIMARY KEY, value VARCHAR(255))");
                stmt.execute("INSERT INTO test (id, value) VALUES (1, 'Hello')");
                ResultSet rs = stmt.executeQuery("SELECT value FROM test WHERE id=1");
                rs.next();
                String value = rs.getString("value");

                assertEquals("Hello", value);
            }
        }
    }
}
