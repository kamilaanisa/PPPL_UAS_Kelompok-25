package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class NotificationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailField = By.id("email");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");

    private By userNotificationText = By.xpath("//*[contains(text(), 'Hari ini jadwal vaksinasi untuk ABC.')]");
    private By detailIconBtn = By.xpath("//table//button");
    private By modalDetailTitle = By.xpath("//*[contains(text(), 'Detail') or contains(text(), 'Vaksinasi')]");

    public NotificationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();

        try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public boolean isNotificationPanelDisplayed() {
        return wait.until(ExpectedConditions.urlContains("notification"));
    }

    public String getUserNotificationMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(userNotificationText)).getText();
    }

    public void clickFirstDetailLogAdmin() {
        wait.until(ExpectedConditions.elementToBeClickable(detailIconBtn)).click();
    }

    public boolean isModalDetailAdminDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(modalDetailTitle)).isDisplayed();
    }
}