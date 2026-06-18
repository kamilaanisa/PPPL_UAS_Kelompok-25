package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.homePage;
import org.example.loginPage;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginStepsDef {
    private final WebDriver driver;
    private final TestContext testContext;
    private loginPage login;
    private homePage home;

    public LoginStepsDef(TestContext testContext) {
        this.testContext = testContext;
        this.driver = testContext.getDriver();
    }

    @Given("user is on the home page")
    public void userIsOnHomePage() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/");
        home = new homePage(driver);
    }

    @When("user clicks the login button on home page")
    public void userClicksLoginButtonOnHomePage() {
        login = home.clickLoginButton();
    }

    @And("user enters username {string} and password {string}")
    public void userEntersUsernameAndPassword(String username, String password) {
        login.setUsername(username);
        login.setPassword(password);
    }

    @And("user submits the login form")
    public void userSubmitsLoginForm() {
        login.clickButton();
    }

    @Then("user should see page path as {string}")
    public void userShouldSeePagePath(String expectedResult) {
        if (expectedResult.startsWith("http://") || expectedResult.startsWith("https://")) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlToBe(expectedResult));
            Assertions.assertEquals(expectedResult, driver.getCurrentUrl());
        } else {
            Assertions.assertTrue(login.isErrorVisible());
        }
    }
}
