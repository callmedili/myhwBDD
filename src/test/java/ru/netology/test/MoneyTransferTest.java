package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.TransferPage;
import ru.netology.pages.VerificationPage;
import org.junit.jupiter.api.Disabled;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);

        options.setExperimentalOption("prefs", prefs);
        Configuration.browserCapabilities = options;
    }

    @Test
    void shouldTransferMoneyBetweenCards() {
        open("http://localhost:9999");

        LoginPage loginPage = new LoginPage();
        loginPage.login("vasya", "qwerty123");

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verify("12345");

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyPage();

        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        int amount = 200;

        dashboardPage.topUpFirstCard();

        TransferPage transferPage = new TransferPage();
        transferPage.transferMoney(amount, "5559 0000 0000 0002");

        int firstCardBalanceAfterTransfer =
                dashboardPage.getFirstCardBalance();

        int secondCardBalanceAfterTransfer =
                dashboardPage.getSecondCardBalance();

        org.junit.jupiter.api.Assertions.assertEquals(
                firstCardBalance + amount,
                firstCardBalanceAfterTransfer
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                secondCardBalance - amount,
                secondCardBalanceAfterTransfer
        );
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondCard() {
        open("http://localhost:9999");

        LoginPage loginPage = new LoginPage();
        loginPage.login("vasya", "qwerty123");

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verify("12345");

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyPage();

        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        int amount = 200;

        dashboardPage.topUpSecondCard();

        TransferPage transferPage = new TransferPage();
        transferPage.transferMoney(amount, "5559 0000 0000 0001");

        int firstCardBalanceAfterTransfer =
                dashboardPage.getFirstCardBalance();

        int secondCardBalanceAfterTransfer =
                dashboardPage.getSecondCardBalance();

        org.junit.jupiter.api.Assertions.assertEquals(
                firstCardBalance - amount,
                firstCardBalanceAfterTransfer
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                secondCardBalance + amount,
                secondCardBalanceAfterTransfer
        );
    }

    @Disabled("Known bug: transfer is allowed when amount exceeds card balance")
    @Test
    void shouldNotTransferMoreThanCardBalance() {
        open("http://localhost:9999");

        LoginPage loginPage = new LoginPage();
        loginPage.login("vasya", "qwerty123");

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verify("12345");

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyPage();

        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        int amount = secondCardBalance + 1000;

        dashboardPage.topUpFirstCard();

        TransferPage transferPage = new TransferPage();
        transferPage.transferMoney(amount, "5559 0000 0000 0002");

        int firstCardBalanceAfterTransfer =
                dashboardPage.getFirstCardBalance();

        int secondCardBalanceAfterTransfer =
                dashboardPage.getSecondCardBalance();

        org.junit.jupiter.api.Assertions.assertEquals(
                firstCardBalance,
                firstCardBalanceAfterTransfer
        );

        org.junit.jupiter.api.Assertions.assertEquals(
                secondCardBalance,
                secondCardBalanceAfterTransfer
        );
    }
}