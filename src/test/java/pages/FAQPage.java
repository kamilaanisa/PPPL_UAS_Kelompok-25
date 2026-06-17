package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FAQPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // ==========================================
    // 1. LOCATOR SISI ADMIN (Berdasarkan HTML Ril)
    // ==========================================

    // Menggunakan aria-label karena sangat spesifik dan aman dari perubahan layout
    private By tambahFaqButton = By.xpath("//button[@aria-label='Tambah FAQ']");

    // Menggunakan atribut placeholder unik dari form input modal
    private By inputPertanyaanField = By.xpath("//input[@placeholder='Masukkan judul FAQ']");
    private By inputJawabanField = By.xpath("//textarea[@placeholder='Tuliskan Deskripsi FAQ di sini...']");

    // Menggunakan kombinasi tag select yang memiliki opsi 'draft' dan 'publish'
    private By statusDropdown = By.xpath("//select[option[@value='draft'] and option[@value='publish']]");

    // Kolom pencarian admin (dipakai untuk mempermudah verifikasi data baru tanpa terganggu pagination)
    private By searchAdminField = By.xpath("//input[@placeholder='Cari judul, atau deskripsi...']");

    // Tombol simpan bertipe submit dengan teks "Simpan"
    private By simpanButton = By.xpath("//button[@type='submit' and text()='Simpan']");


    // ==========================================
    // 2. LOCATOR SISI USER / PASIEN (Berdasarkan HTML Ril)
    // ==========================================

    // Navigasi menu FAQ menggunakan atribut href
    private By navFaqButton = By.xpath("//a[@href='/faq']");

    // Kolom pencarian user berdasarkan placeholder pencarian terkait
    private By searchUserField = By.xpath("//input[@placeholder='Cari Pertanyaan Terkait...']");

    // Elemen detail jawaban (menggunakan class utama wadah jawaban)
    private By detailJawabanContainer = By.xpath("//div[contains(@class, 'border-accent-yellow-300') and contains(@class, 'rounded-b-lg')]");

    // Dynamic Locator untuk hasil pencarian berdasarkan teks Pertanyaan yang dicari
    private By faqResultRow(String pertanyaan) {
        return By.xpath("//button[contains(@class, 'border-accent-yellow-300')]//span[text()='" + pertanyaan + "']");
    }

    // Dynamic Locator untuk memverifikasi teks jawaban spesifik di dalam detail komponen
    private By faqAnswerText(String jawaban) {
        return By.xpath("//div[contains(@class, 'border-accent-yellow-300')]//*[text()='" + jawaban + "']");
    }

    // Constructor
    public FAQPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ==========================================
    // METHOD AKSI SISI ADMIN
    // ==========================================

    public boolean isAtManajemenFAQPage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(tambahFaqButton)).isDisplayed();
    }

    public void klikTambahFAQ() {
        wait.until(ExpectedConditions.elementToBeClickable(tambahFaqButton)).click();
    }

    public void isiPertanyaan(String pertanyaan) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputPertanyaanField)).sendKeys(pertanyaan);
    }

    public void isiJawaban(String jawaban) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputJawabanField)).sendKeys(jawaban);
    }

    public void pilihStatusPublish() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(statusDropdown));
        Select selectStatus = new Select(element);

        // Memilih opsi "Publish" berdasarkan value HTML-nya
        selectStatus.selectByValue("publish");
    }

    public void klikSimpan() {
        wait.until(ExpectedConditions.elementToBeClickable(simpanButton)).click();
        // Beri jeda sinkronisasi agar proses simpan data selesai masuk list backend
        try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public boolean isFaqBerhasilDitambahkan(String pertanyaan) {
        // 1. Ketik judul pertanyaan baru di kolom pencarian admin agar menyaring tabel
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchAdminField));
        searchInput.clear();
        searchInput.sendKeys(pertanyaan);

        // Jeda kecil 1 detik demi memastikan event AJAX/filter frontend selesai memproses pencarian
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

        // 2. PERBAIKAN DI SINI: Mencari teks pertanyaan di elemen apa pun ( //* ) yang ada di dalam table
        By elemenFaqBaru = By.xpath("//table//*[contains(text(), '" + pertanyaan + "')]");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(elemenFaqBaru)).isDisplayed();
    }

    public void kosongkanPertanyaan() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputPertanyaanField));
        input.clear();
    }

    // Mengambil pesan error validasi HTML5 bawaan browser (required attribute)
    public String getPertanyaanValidationError() {
        WebElement input = driver.findElement(inputPertanyaanField);
        return input.getAttribute("validationMessage");
    }

    // ==========================================
    // METHOD AKSI SISI USER
    // ==========================================

    public void klikNavigasiFAQ() {
        wait.until(ExpectedConditions.elementToBeClickable(navFaqButton)).click();
    }

    public boolean isAtUserFAQPage() {
        return wait.until(ExpectedConditions.urlContains("/faq"));
    }

    public void cariKataKunci(String kataKunci) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchUserField));
        searchInput.clear();
        searchInput.sendKeys(kataKunci);
    }

    public boolean isFaqResultDisplayed(String pertanyaan) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(faqResultRow(pertanyaan))).isDisplayed();
    }

    public void klikHasilFaq(String pertanyaan) {
        wait.until(ExpectedConditions.elementToBeClickable(faqResultRow(pertanyaan))).click();
    }

    public boolean isDetailJawabanMuncul() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(detailJawabanContainer)).isDisplayed();
    }

    public String getDetailJawabanText(String jawaban) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(faqAnswerText(jawaban))).getText();
    }
}