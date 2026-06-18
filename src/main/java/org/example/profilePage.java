package org.example;

import org.openqa.selenium.WebDriver;

public class profilePage extends BasePage {

    public profilePage(WebDriver driver) {
        super(driver);
    }

    public void clickProfileButton() {
        click(PageLocators.buttonProfile);
    }

    public void clickEditProfileButton() {
        click(PageLocators.buttonEditProfile);
    }

    public void setUsername(String username) {
        type(PageLocators.userNameInput, username);
    }

    public void setPhoneNumber(String phoneNumber) {
        type(PageLocators.phoneNumberInput, phoneNumber);
    }

    public void clickSaveChangeButton() {
        click(PageLocators.buttonSaveChange);
    }


    public boolean hasStatusMessage(String expectedMessage) {
    try {
        org.openqa.selenium.By dynamicMessageLocator = org.openqa.selenium.By.xpath(
                "//*[contains(text(), '" + expectedMessage + "')]"
        );
        return isVisible(dynamicMessageLocator);
    } catch (Exception e) {
        return false;
    }
}
    public void changePasswordForm(String currentPwd, String newPwd, String confirmPwd) {
        type(PageLocators.currenPassword, currentPwd);
        type(PageLocators.newPassword, newPwd);
        type(PageLocators.confirmPassword, confirmPwd);
    }
}
