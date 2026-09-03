package ru.netology.test;

import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.pages.DashboardPage;
import ru.netology.pages.LoginPage;
import ru.netology.pages.TransferPage;
import ru.netology.pages.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class MoneyTransferTest {

    private DashboardPage loginAndOpenDashboard() {
        open("http://localhost:9999");

        LoginPage loginPage = new LoginPage();
        loginPage.login(
                DataHelper.getLogin(),
                DataHelper.getPassword()
        );

        VerificationPage verificationPage = new VerificationPage();
        verificationPage.verify(DataHelper.getVerificationCode());

        DashboardPage dashboardPage = new DashboardPage();
        dashboardPage.verifyPage();

        return dashboardPage;
    }

    @Test
    void shouldTransferMoneyBetweenCards() {
        DashboardPage dashboardPage = loginAndOpenDashboard();

        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        int amount = DataHelper.getTransferAmount();

        dashboardPage.topUpFirstCard();

        TransferPage transferPage = new TransferPage();
        transferPage.transferMoney(
                amount,
                DataHelper.getSecondCardNumber()
        );

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
        DashboardPage dashboardPage = loginAndOpenDashboard();

        int firstCardBalance = dashboardPage.getFirstCardBalance();
        int secondCardBalance = dashboardPage.getSecondCardBalance();

        int amount = DataHelper.getTransferAmount();

        dashboardPage.topUpSecondCard();

        TransferPage transferPage = new TransferPage();
        transferPage.transferMoney(
                amount,
                DataHelper.getFirstCardNumber()
        );

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
}