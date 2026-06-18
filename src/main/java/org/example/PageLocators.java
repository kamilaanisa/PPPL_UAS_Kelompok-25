package org.example;

import org.openqa.selenium.By;

public final class PageLocators {
    private PageLocators() {
    }

    public static final By username = By.id("email");
    public static final By password = By.id("password");
    public static final By buttonSummit = By.xpath("//button[@type='submit']");
    public static final By buttonLogin = By.xpath("//a[text()='Login']");
    public static final By invalidLogin = By.xpath("//div[text()='These credentials do not match our records.']");

    // profile test scenario
    public static final By buttonProfile = By.cssSelector("[aria-label='Go To Profile']");
    public static final By buttonEditProfile = By.xpath("//*[contains(text(), 'Edit Profile')]");
    public static final By userNameInput = By.name("username");
    public static final By phoneNumberInput = By.name("phone_number");
    public static final By formatNumberInvalid = By.xpath("//p[contains(text(), 'tidak valid')]");
    public static final By minimalDigit= By.xpath("//p[contains(text(), 'minimal 10 digit')]");

    public static final By userNameDisplay = By.xpath("//div[label[contains(text(), 'Username')]]//div[contains(@class, 'relative z-10')]");

    public static final By emailDisplay = By.xpath("//div[label[contains(text(), 'E-mail')]]//div[contains(@class, 'relative z-10')]");
    public static final By successfullToast = By.xpath("//*[contains(text(), 'berhasil diperbarui')]");

    // password
    public static final By currenPassword =By.name("current_password");
    public static final By newPassword =By.name("password");
    public static final By confirmPassword= By.name("password_confirmation");


    public static final By buttonSaveChange = By.xpath("//button[span[text()='Simpan Perubahan']]");

}

