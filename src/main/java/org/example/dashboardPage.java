package org.example;

import org.openqa.selenium.WebDriver;

public class dashboardPage  extends BasePage {

    public dashboardPage(WebDriver driver) {
        super(driver);
    }

    public void clickButtonLogin(String query){
        type(PageLocators.buttonLogin, query);
    }
}
