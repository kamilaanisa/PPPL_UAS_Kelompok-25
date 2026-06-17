package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import Pages.InvoicePages;
import org.junit.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;

public class InvoiceSteps {
    WebDriver driver;
    InvoicePages pages;

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Given("Admin berada di Halaman Login")
    public void admin_berada_di_halaman_login() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.setBinary("C:\\Program Files\\BraveSoftware\\Brave-Browser\\Application\\brave.exe");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get("https://compro-drhfanina-pad1.vercel.app/"); 
        pages = new InvoicePages(driver);
    }

    @When("Admin login menggunakan username {string} dan password {string}")
    public void admin_login(String user, String pass) {
        pages.goToLoginPage();
        pages.login(user, pass);
    }

    @And("Admin diarahkan ke Dashboard dan menekan menu Manajemen Invoice")
    public void ke_manajemen_invoice() {
        pages.goToInvoiceManagement();
    }

    @And("Admin memilih pasien {string} dan hewan {string}")
    public void pilih_pasien_hewan(String pasien, String hewan) {
        pages.setupDataPasien(pasien, hewan);
    }

    @And("Admin mengisi tanggal invoice dan jatuh tempo")
    public void isi_tanggal() {
        pages.isiTanggalInvoice();
    }

    @Given("Admin berada di form buat Invoice")
    public void admin_berada_di_form_buat_invoice() {
        // Pre-condition: Login → Dashboard → Invoice → Buat Invoice → Pilih Pasien & Hewan → Isi Tanggal
        admin_berada_di_halaman_login();
        pages.goToLoginPage();
        pages.login("admin@klinikdrfanina.com", "password123");
        pages.goToInvoiceManagement();
        pages.setupDataPasien("testing", "asd");
        pages.isiTanggalInvoice();
    }

    @And("Admin menginput item layanan {string} dengan harga {string}, jumlah {string}, PPN {string}, dan diskon {string}")
    public void input_layanan(String namaItem, String harga, String qty, String ppn, String diskon) {
        pages.inputDetailItem(namaItem, harga, qty, ppn, diskon);
    }

    @Then("Sistem menghitung total otomatis menjadi {string}")
    public void verifikasi_total(String expectedTotal) {
        String actualTotal = pages.getTotalKalkulasi();
        String cleanActualTotal = actualTotal.replaceAll("[^0-9]", "");
        Assert.assertEquals(expectedTotal, cleanActualTotal);
    }

    @When("Admin menekan tombol Buat Invoice")
    public void klik_buat_invoice() {
        pages.submitInvoice();
    }

    @Then("Sistem menampilkan Halaman Detail PDF Invoice tanpa error")
    public void verifikasi_pdf() {
        Assert.assertTrue("Invoice tidak berhasil dibuat atau tidak muncul di dashboard!", pages.isPdfGenerated());
    }
    
    @Then("Sistem menolak dengan pesan error format nominal")
    public void verifikasi_error_nominal() {
        Assert.assertTrue("Pesan error format nominal tidak muncul!", pages.isErrorNominalDisplayed());
    }

    @When("Admin memasukkan diskon {string} lalu simpan")
    public void input_diskon_saja(String diskon) {
        pages.inputDiskon(diskon);
    }

    @Then("Sistem menampilkan pop up notifikasi berhasil")
    public void verifikasi_notifikasi_berhasil() {
        Assert.assertTrue("Pop up notifikasi berhasil tidak muncul!", pages.isSuccessNotificationDisplayed());
    }

    @Then("Sistem menampilkan pop up notifikasi gagal karena format nominal tidak valid")
    public void verifikasi_notifikasi_gagal_nominal() {
        Assert.assertTrue("Pop up notifikasi gagal tidak muncul!", pages.isErrorNotificationDisplayed());
    }
}