package ru.netology.sqltests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String dbUrl = "jdbc:mysql://localhost:3306/test_db";
    private String dbUser = "app";
    private String dbPassword = "app_pass";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    private String getLatestAuthCode(String login) throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(
                    "SELECT auth_codes.code FROM auth_codes " +
                            "JOIN users ON auth_codes.user_id = users.id " +
                            "WHERE users.login = '" + login + "' ORDER BY auth_codes.created DESC LIMIT 1;");
            if (rs.next()) {
                return rs.getString("code");
            }
        }
        return null;
    }

    @Test
    void testSuccessfulLogin() throws Exception {
        driver.findElement(By.cssSelector("[name='login']")).sendKeys("vasya");
        driver.findElement(By.cssSelector("[name='password']")).sendKeys("qwerty123");

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test-id='action-login']"))).click();

        // Ожидание появления поля ввода кода
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[name='code']")));

        String authCode = getLatestAuthCode("vasya");
        if (authCode == null) {
            throw new RuntimeException("Не найден код авторизации");
        }

        driver.findElement(By.cssSelector("[name='code']")).sendKeys(authCode);
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test-id='action-verify']"))).click();

        // Ожидание загрузки "Личного кабинета"
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='dashboard']")));

        assertTrue(driver.findElement(By.cssSelector("[data-test-id='dashboard']")).isDisplayed(),
                "Ошибка: вход в систему не выполнен!");
    }
}
