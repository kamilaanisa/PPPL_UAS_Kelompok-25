package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import pages.NotificationPage;

public class NotificationSteps {
    private WebDriver driver = Hooks.getDriver();
    private NotificationPage notificationPage = new NotificationPage(driver);

    @Given("User melakukan login dengan email {string} dan password {string}")
    public void userMelakukanLogin(String email, String password) {
        driver.get("https://compro-drhfanina-pad1.vercel.app/auth/login");
        notificationPage.login(email, password);
    }

    @And("User berada di Dashboard")
    public void userBeradaDiDashboard() {
    }

    @When("User mengklik ikon lonceng notifikasi di bilah navigasi utama")
    public void userKlikIkonLonceng() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/notifications");
    }

    @Then("Sistem menampilkan panel daftar pesan pengingat")
    public void sistemMenampilkanPanel() {
        Assertions.assertTrue(notificationPage.isNotificationPanelDisplayed());
    }

    @And("Isi teks pesan pengingat harus berbunyi {string}")
    public void verifikasiIsiTeksPesan(String expectedMessage) {
        Assertions.assertEquals(expectedMessage, notificationPage.getUserNotificationMessage());
    }

    @Given("Admin melakukan login dengan email {string} dan password {string}")
    public void adminMelakukanLogin(String email, String password) {
        driver.get("https://compro-drhfanina-pad1.vercel.app/auth/login");
        notificationPage.login(email, password);
    }

    @And("Admin berada di halaman Log Notifikasi")
    public void adminBeradaDiHalamanLogNotifikasi() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/dashboard/notification");
    }

    @When("Admin mengklik ikon detail info pada baris log aktivitas teratas")
    public void adminKlikIkonDetail() {
        notificationPage.clickFirstDetailLogAdmin();
    }

    @Then("Jendela modal Detail Notifikasi harus muncul di layar")
    public void modalMunculDiLayar() {
        Assertions.assertTrue(notificationPage.isModalDetailAdminDisplayed());
    }

    @And("Jendela modal menampilkan detail isi pesan secara komprehensif")
    public void jendelaModalMenampilkanIsiPesan() {
        Assertions.assertTrue(notificationPage.isModalDetailAdminDisplayed());
    }
}