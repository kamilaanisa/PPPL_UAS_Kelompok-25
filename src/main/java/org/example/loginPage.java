package org.example;

import org.openqa.selenium.WebDriver;

public class loginPage extends BasePage {

    public loginPage(WebDriver driver) {
        super(driver);
    }

    public void setUsername(String query) {
        type(PageLocators.username, query);
    }

    public void setPassword(String query) {
        type(PageLocators.password, query);
    }

    public void  clickButton() {
        click(PageLocators.buttonSummit);
//        return new dashboardPage(driver);
    }
//    public void clickButtonAsUser(){
//        click(PageLocators.buttonSummit);
//    }

    public boolean isErrorVisible() {
        return isVisible(PageLocators.invalidLogin);
    }
}
