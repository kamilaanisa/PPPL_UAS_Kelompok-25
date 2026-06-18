package org.example;

import org.openqa.selenium.WebDriver;

public class homePage extends BasePage {

    public homePage(WebDriver driver) {
        super(driver);
    }

    public loginPage clickLoginButton() {
        click(PageLocators.buttonLogin);
        return new loginPage(driver);
    }


}
