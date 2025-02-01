package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Condition.visible;

public class DashboardPage {
    private SelenideElement dashboardHeader = $("[data-test-id='dashboard']");

    public DashboardPage() {
        dashboardHeader.shouldBe(visible); // Ожидание появления Личного кабинета
    }

    public boolean isDashboardVisible() {
        return dashboardHeader.isDisplayed();
    }
}

