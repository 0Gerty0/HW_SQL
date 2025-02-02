package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class DashboardPage {
    private final SelenideElement dashboardHeader = $("[data-test-id='dashboard']");

    public void checkDashboardVisible() {
        dashboardHeader.shouldBe(visible);
    }
}


