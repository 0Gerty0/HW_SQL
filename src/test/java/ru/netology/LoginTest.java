package ru.netology;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.helpers.DBHelper;
import ru.netology.helpers.DataHelper;
import ru.netology.pages.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDown() {
        DBHelper.clearDatabase();
    }

    @Test
    void testSuccessfulLogin() {
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo.getLogin(), authInfo.getPassword());

        var verificationCode = DBHelper.getVerificationCode(authInfo.getLogin());
        assertNotNull(verificationCode, "Ошибка: код авторизации отсутствует!");

        var dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkDashboardVisible();
    }
}
