package ru.netology.sqltests;

import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.helpers.DBHelper;
import ru.netology.helpers.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.VerificationPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    @BeforeEach
    void setUp() {
        Selenide.open("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test
    void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage();
        VerificationPage verificationPage = loginPage.validLogin(DataHelper.getValidLogin(), DataHelper.getValidPassword());

        String authCode = DBHelper.getLatestAuthCode(DataHelper.getValidLogin());
        if (authCode == null) {
            throw new RuntimeException("Не найден код авторизации");
        }

        DashboardPage dashboardPage = verificationPage.validVerify(authCode);
        assertTrue(dashboardPage.isDashboardVisible(), "Ошибка: вход в систему не выполнен!");
    }
}

