package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.homePage;
import org.example.loginPage;
import org.example.profilePage;
import org.example.PageLocators;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ProfileStepsDef {
    private final WebDriver driver;
    private final TestContext testContext;
    private homePage home;
    private loginPage login;
    private profilePage profile;
    private String newPasswordAttempted = null;
    private boolean isPasswordChanged = false;

    public ProfileStepsDef(TestContext testContext) {
        this.testContext = testContext;
        this.driver = testContext.getDriver();
    }

    @Given("user is already logged in to the system")
    public void userIsAlreadyLoggedInToTheSystem() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/");
        home = new homePage(driver);
        login = home.clickLoginButton();
        login.setUsername("lala@tes.com");
        login.setPassword("indonesia");
        login.clickButton();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlToBe("https://compro-drhfanina-pad1.vercel.app/"));
    }

    @And("user navigates to the profile page")
    public void userNavigatesToTheProfilePage() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/profile");
        profile = new profilePage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlToBe("https://compro-drhfanina-pad1.vercel.app/profile"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PageLocators.buttonEditProfile));
    }


    @Then("user should see their username")
    public void userShouldSeeTheirUsername() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(PageLocators.userNameDisplay, "larasati"));
        String actualUsername = driver.findElement(PageLocators.userNameDisplay).getText();
        Assertions.assertEquals("larasati", actualUsername);
    }

    @And("user should see their registered email")
    public void userShouldSeeTheirRegisteredEmail() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(PageLocators.emailDisplay, "lala@tes.com"));
        String actualEmail = driver.findElement(PageLocators.emailDisplay).getText();
        Assertions.assertEquals("lala@tes.com", actualEmail);
    }

    @When("user updates their name to {string}")
    public void userUpdatesTheirNameTo(String newName) {
        profile.clickEditProfileButton();
        profile.setUsername(newName);
    }

    @And("user updates their phone number to {string}")
    public void userUpdatesTheirPhoneNumberTo(String newPhone) {
        profile.setPhoneNumber(newPhone);
    }

    @And("user clicks the save button")
    public void userClicksTheSaveButton() {
        profile.clickSaveChangeButton();
    }

    @Then("user should see a {string}")
    public void userShouldSeeAStatusMessage(String expectedMessage) {
        boolean statusVisible = profile.hasStatusMessage(expectedMessage);
        Assertions.assertTrue(statusVisible,
            "Expected status message '" + expectedMessage + "' was not found or visible.");
        if (statusVisible && expectedMessage.equalsIgnoreCase("Profile berhasil diperbarui") && newPasswordAttempted != null) {
            this.isPasswordChanged = true;
        }
    }

    @And("user should see their profile username updated to {string}")
    public void userShouldSeeTheirProfileUsernameUpdatedTo(String expectedUsername) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(PageLocators.userNameDisplay, expectedUsername));

        String actualUsername = driver.findElement(PageLocators.userNameDisplay).getText();
        Assertions.assertEquals(expectedUsername, actualUsername);
    }
    @When("user updates their password with current password {string}, new password {string}, and confirmation {string}")
    public void userUpdatesTheirPasswordWithCurrentPasswordNewPasswordAndConfirmation(String currentPwd, String newPwd, String confirmPwd) {
        profile.clickEditProfileButton();
        profile.changePasswordForm(currentPwd, newPwd, confirmPwd);
        this.newPasswordAttempted = newPwd;
    }

    @After(value = "@profile and not @update-password", order = 1)
    public void resetProfileData() {

        if (driver != null) {
            driver.get("https://compro-drhfanina-pad1.vercel.app/profile");
            profile = new profilePage(driver);


            profile.clickEditProfileButton();
            profile.setUsername("larasati");
            profile.clickSaveChangeButton();
        }
    }
//    @After (value = "@profile and @update-password", order = 1)
//    public void resetPasswordData(){
//        if (driver != null && isPasswordChanged && newPasswordAttempted != null) {
//            driver.get("https://compro-drhfanina-pad1.vercel.app/profile");
//            profile = new profilePage(driver);
//            profile.clickEditProfileButton();
//
//
//            profile.changePasswordForm(newPasswordAttempted, "indonesia", "indonesia");
//            profile.clickSaveChangeButton();
//        }

//    }
}
