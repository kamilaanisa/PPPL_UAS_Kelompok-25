package stepDefinitions;

import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import pages.FAQPage;
import pages.NotificationPage;

public class FAQSteps {
    private WebDriver driver = Hooks.getDriver();
    private FAQPage faqPage = new FAQPage(driver);
    private NotificationPage notificationPage = new NotificationPage(driver);

    // --- SEGMEN ADMIN LOGIN & DASHBOARD ---

    @Given("Admin berada di halaman Login")
    public void adminBeradaDiHalamanLogin() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/auth/login");
    }

    @When("Admin memasukkan username dan password yang valid")
    public void adminMemasukkanUsernameDanPasswordYangValid() {
        notificationPage.login("admin@klinikdrfanina.com", "password123");
    }

    @And("Admin menekan tombol login")
    public void adminMenekanTombolLogin() {
        // Handle otomatis oleh method login()
    }

    @Then("Admin berhasil masuk ke dashboard admin")
    public void adminBerhasilMasukKeDashboardAdmin() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/dashboard");
    }

    // --- SEGMEN MANAJEMEN FAQ (ADMIN) ---

    @Given("Admin berada di halaman Manajemen FAQ")
    public void adminBeradaDiHalamanManajemenFAQ() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/dashboard/faq");
        Assertions.assertTrue(faqPage.isAtManajemenFAQPage(), "Admin tidak berada di halaman Manajemen FAQ");
    }

    @When("Admin menambahkan pertanyaan baru {string}")
    public void adminMenambahkanPertanyaanBaru(String pertanyaan) {
        faqPage.klikTambahFAQ();
        faqPage.isiPertanyaan(pertanyaan);
    }

    @And("Admin menambahkan jawaban baru {string}")
    public void adminMenambahkanJawabanBaru(String jawaban) {
        faqPage.isiJawaban(jawaban);
        faqPage.pilihStatusPublish();
    }

    @And("Admin menekan tombol simpan")
    public void adminMenekanTombolSimpan() {
        faqPage.klikSimpan();
    }

    @Then("Data FAQ baru berhasil ditambahkan")
    public void dataFAQBaruBerhasilDitambahkan() {
        Assertions.assertTrue(faqPage.isFaqBerhasilDitambahkan("Bagaimana cara reset password?"));
    }

    // --- SEGMEN INTERAKSI PENCARIAN (USER) - SUDAH DISESUAIKAN DENGAN ERROR ---

    @Given("User berada di Landing Page")
    public void userBeradaDiLandingPage() {
        driver.get("https://compro-drhfanina-pad1.vercel.app/");
    }

    @Then("User dapat melihat navigasi menuju halaman FAQ atau Pusat Bantuan")
    public void userDapatMelihatNavigasiMenujuHalamanFAQAtauPusatBantuan() {
        faqPage.klikNavigasiFAQ();
    }

    @Given("User berada di halaman FAQ atau Pusat Bantuan")
    public void userBeradaDiHalamanFAQAtauPusatBantuan() {
        // Menunggu perpindahan url agar asersi presisi
        Assertions.assertTrue(faqPage.isAtUserFAQPage(), "User tidak berada di halaman FAQ");
    }

    @When("User mengetik kata kunci {string} pada kolom pencarian")
    public void userMengetikKataKunciPadaKolomPencarian(String kataKunci) {
        faqPage.cariKataKunci(kataKunci);
    }

    @Then("Sistem menampilkan FAQ yang relevan dengan kata kunci")
    public void sistemMenampilkanFAQYangRelevanDenganKataKunci() {
        Assertions.assertTrue(faqPage.isFaqResultDisplayed("Bagaimana cara reset password?"));
    }

    @When("User mengklik hasil FAQ {string}")
    public void userMengklikHasilFAQ(String pertanyaan) {
        faqPage.klikHasilFaq(pertanyaan);
    }

    @Then("Sistem menampilkan detail jawaban FAQ")
    public void sistemMenampilkanDetailJawabanFAQ() {
        Assertions.assertTrue(faqPage.isDetailJawabanMuncul());
    }

    @And("User dapat membaca jawaban {string}")
    public void userDapatMembacaJawaban(String expectedJawaban) {
        String actualJawaban = faqPage.getDetailJawabanText(expectedJawaban);
        Assertions.assertEquals(expectedJawaban, actualJawaban);
    }

    // =======================================================
    // ALIRAN TRANSISI: DARI USER KEMBALI KE ADMIN
    // =======================================================

    @Given("Admin kembali ke halaman Manajemen FAQ")
    public void adminKembaliKeHalamanManajemenFAQ() {
        // Buka kembali URL manajemen FAQ admin karena sebelumnya browser berada di sisi user
        driver.get("https://compro-drhfanina-pad1.vercel.app/dashboard/faq");

        // Pastikan halaman admin sudah terbuka dengan benar
        Assertions.assertTrue(faqPage.isAtManajemenFAQPage(), "Gagal kembali ke halaman Manajemen FAQ Admin");
    }

    @When("Admin mengosongkan kolom Pertanyaan")
    public void adminMengosongkanKolomPertanyaan() {
        faqPage.klikTambahFAQ(); // Klik tombol "+ Tambah FAQ" untuk memunculkan modal form lagi
        faqPage.kosongkanPertanyaan(); // Kosongkan inputnya
    }

    @Then("Sistem menampilkan pesan error validasi")
    public void sistemMenampilkanPesanErrorValidasi() {
        String errorMessage = faqPage.getPertanyaanValidationError();

        // Validasi bahwa form tidak bisa disubmit (HTML5 required aktif)
        Assertions.assertFalse(errorMessage.isEmpty(), "Pesan error validasi tidak muncul!");
        System.out.println("Pesan error validasi HTML5 yang muncul: " + errorMessage);
    }
}