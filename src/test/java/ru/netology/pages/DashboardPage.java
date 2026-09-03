package ru.netology.pages;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {

    private final SelenideElement heading =
            $("h1");
    private final SelenideElement firstCard =
            $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private final SelenideElement secondCard =
            $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");

    public void verifyPage() {
        heading.shouldHave(com.codeborne.selenide.Condition.text("Ваши карты"));
    }

    public int getCardBalance(SelenideElement card) {
        String text = card.getText();
        String balance = text.substring(
                text.indexOf("баланс: ") + 8,
                text.indexOf(" р.")
        );

        return Integer.parseInt(balance);
    }

    public int getFirstCardBalance() {
        return getCardBalance(firstCard);
    }

    public int getSecondCardBalance() {
        return getCardBalance(secondCard);
    }

    public void topUpFirstCard() {
        firstCard.$("button").click();
    }

    public void topUpSecondCard() {
        secondCard.$("button").click();
    }
}