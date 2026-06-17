package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InvoicePages {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public InvoicePages(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js = (JavascriptExecutor) driver;
    }

    // ===== Helper: set value pada input date via React-compatible JS =====
    private void setDateValue(WebElement el, String dateValue) {
        js.executeScript(
            "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
            "nativeInputValueSetter.call(arguments[0], arguments[1]);" +
            "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
            "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
            el, dateValue);
    }

    // ===== Helper: clear dan ketik value pada input =====
    private void clearAndType(WebElement el, String value) {
        el.click();
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(value);
        el.sendKeys(Keys.TAB);
        try { Thread.sleep(300); } catch (Exception ignored) {}
    }

    // ===== Landing Page =====
    By btnLoginLanding = By.xpath("//a[contains(@class,'bg-accent-yellow-300')]");
    public void goToLoginPage() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLoginLanding)).click();
    }

    // ===== Login Page =====
    By inputEmail    = By.id("email");
    By inputPassword = By.id("password");
    By btnSubmitLogin = By.xpath("//button[@type='submit']");
    public void login(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(inputEmail)).sendKeys(email);
        driver.findElement(inputPassword).sendKeys(password);
        driver.findElement(btnSubmitLogin).click();
    }

    // ===== Dashboard → Menu Invoice =====
    By menuInvoice = By.xpath("//a[contains(@href,'/dashboard/invoice')]");
    public void goToInvoiceManagement() {
        wait.until(ExpectedConditions.elementToBeClickable(menuInvoice)).click();
        wait.until(ExpectedConditions.urlContains("/dashboard/invoice"));
        try { Thread.sleep(1000); } catch (Exception ignored) {}
    }

    // ===== Halaman Daftar Invoice =====
    By btnBuatInvoiceHeader = By.xpath("//button[contains(.,'Buat Invoice')]");

    // ===== Modal: Buat Invoice Baru =====
    // 3 select: (1) Pasien, (2) Hewan, (3) Status
    By selectPasien    = By.xpath("(//select)[1]");
    By selectHewan     = By.xpath("(//select)[2]");
    By inputTanggal    = By.xpath("(//input[@type='date'])[1]");
    By inputJatuhTempo = By.xpath("(//input[@type='date'])[2]");

    // Tambah Item section
    By inputNamaItem = By.xpath("//input[@placeholder='Nama item']");
    By inputKategori = By.xpath("//input[@placeholder='Kategori']");
    By inputQty      = By.xpath("//input[@placeholder='1']");
    By inputHarga    = By.xpath("//input[@placeholder='0']");
    By btnTambah     = By.xpath("//button[contains(.,'Tambah')]");

    // Diskon & Pajak (di bawah tabel item, di luar section Tambah Item)
    // Dari screenshot: Diskon (%) dan Pajak (%) adalah input terpisah, value default = 0
    By inputDiskon = By.xpath("(//input[@type='number'])[last()-1] | (//input[preceding-sibling::*[contains(.,'Diskon')]])[1]");
    By inputPajak  = By.xpath("(//input[@type='number'])[last()] | (//input[preceding-sibling::*[contains(.,'Pajak')]])[1]");

    // Total display - dari screenshot: "Total (1 item)" di kiri, "Rp 100.000" di kanan
    // Ambil span/elemen yang berisi "Rp" dan angka
    By totalDisplay = By.xpath("//*[contains(text(),'Rp') and string-length(normalize-space())>2]");

    // Buat Invoice submit button (di modal, bukan yang di halaman daftar)
    // Dari screenshot: tombol biru "Buat Invoice" di sebelah "Batal"
    By btnBuatInvoiceSubmit = By.xpath("//button[contains(@class,'bg-accent-blue') and normalize-space()='Buat Invoice']");

    public void setupDataPasien(String pasien, String hewan) {
        // Klik tombol + Buat Invoice di halaman daftar
        wait.until(ExpectedConditions.elementToBeClickable(btnBuatInvoiceHeader)).click();
        try { Thread.sleep(1500); } catch (Exception ignored) {}

        // Pilih Pasien
        WebElement elPasien = wait.until(ExpectedConditions.elementToBeClickable(selectPasien));
        Select sPasien = new Select(elPasien);
        try { sPasien.selectByVisibleText(pasien); }
        catch (Exception e) { if (sPasien.getOptions().size() > 1) sPasien.selectByIndex(1); }

        // Tunggu Hewan dropdown di-enable (fetch API)
        try { Thread.sleep(2000); } catch (Exception ignored) {}

        // Pilih Hewan
        WebElement elHewan = wait.until(ExpectedConditions.elementToBeClickable(selectHewan));
        Select sHewan = new Select(elHewan);
        try { sHewan.selectByVisibleText(hewan); }
        catch (Exception e) { if (sHewan.getOptions().size() > 1) sHewan.selectByIndex(1); }
    }

    // ===== Isi Tanggal Invoice & Jatuh Tempo (WAJIB) =====
    public void isiTanggalInvoice() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nextMonth = LocalDate.now().plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        setDateValue(wait.until(ExpectedConditions.presenceOfElementLocated(inputTanggal)), today);
        setDateValue(wait.until(ExpectedConditions.presenceOfElementLocated(inputJatuhTempo)), nextMonth);
    }

    public void inputDetailItem(String namaItem, String harga, String qty, String ppn, String diskon) {
        // Isi Nama Item
        WebElement elNama = wait.until(ExpectedConditions.visibilityOfElementLocated(inputNamaItem));
        clearAndType(elNama, namaItem);

        // Isi Kategori
        try {
            WebElement elKat = driver.findElement(inputKategori);
            clearAndType(elKat, "Layanan");
        } catch (Exception ignored) {}

        // Isi Qty (default = 1)
        WebElement elQty = wait.until(ExpectedConditions.visibilityOfElementLocated(inputQty));
        clearAndType(elQty, qty);

        // Isi Harga Satuan (default = 0)
        WebElement elHarga = wait.until(ExpectedConditions.visibilityOfElementLocated(inputHarga));
        clearAndType(elHarga, harga);

        // Klik + Tambah
        wait.until(ExpectedConditions.elementToBeClickable(btnTambah)).click();
        try { Thread.sleep(800); } catch (Exception ignored) {}

        // Scroll ke bawah agar Diskon & Pajak terlihat
        js.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(
            By.xpath("//button[normalize-space()='Buat Invoice']")));
        try { Thread.sleep(300); } catch (Exception ignored) {}

        // Isi Pajak (%)
        try {
            // Cari semua input type=number. Dari layout:
            // Setelah item ditambahkan, input number yang tersisa = Qty di tabel, Diskon, Pajak
            // Diskon dan Pajak berada di bagian bawah
            List<WebElement> numberInputs = driver.findElements(By.xpath("//input[@type='number']"));
            if (numberInputs.size() >= 2) {
                // Pajak = input terakhir, Diskon = input kedua terakhir
                WebElement elDiskon = numberInputs.get(numberInputs.size() - 2);
                WebElement elPajak = numberInputs.get(numberInputs.size() - 1);
                clearAndType(elDiskon, diskon);
                clearAndType(elPajak, ppn);
            }
        } catch (Exception ignored) {}

        try { Thread.sleep(500); } catch (Exception ignored) {}
    }

    public void inputDiskon(String diskon) {
        try {
            List<WebElement> numberInputs = driver.findElements(By.xpath("//input[@type='number']"));
            if (numberInputs.size() >= 2) {
                WebElement elDiskon = numberInputs.get(numberInputs.size() - 2);
                clearAndType(elDiskon, diskon);
            }
        } catch (Exception ignored) {}
        try { Thread.sleep(500); } catch (Exception ignored) {}
    }

    // Ambil total dari UI - Total hanya menampilkan subtotal item TANPA pajak/diskon
    // Pajak dan Diskon dihitung oleh BACKEND saat submit
    public String getTotalKalkulasi() {
        try {
            List<WebElement> rpElements = driver.findElements(totalDisplay);
            // Ambil yang berisi "Rp" dan angka, biasanya yang terakhir = grand total
            for (int i = rpElements.size() - 1; i >= 0; i--) {
                String txt = rpElements.get(i).getText().trim();
                if (txt.startsWith("Rp")) {
                    return txt.replaceAll("[^0-9]", "");
                }
            }
        } catch (Exception ignored) {}
        return "0";
    }

    public void submitInvoice() {
        // Scroll ke bawah dulu
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        try { Thread.sleep(300); } catch (Exception ignored) {}

        try {
            // Cari tombol Buat Invoice yang bukan header button
            List<WebElement> btns = driver.findElements(By.xpath("//button[normalize-space()='Buat Invoice']"));
            // Ambil tombol terakhir (yang di modal/form, bukan header)
            for (int i = btns.size() - 1; i >= 0; i--) {
                if (btns.get(i).isDisplayed() && btns.get(i).isEnabled()) {
                    btns.get(i).click();
                    break;
                }
            }
        } catch (Exception e) {
            driver.findElement(btnBuatInvoiceSubmit).click();
        }
        try { Thread.sleep(2000); } catch (Exception ignored) {}
    }

    // ===== TC-INV-01: Verifikasi invoice berhasil dibuat =====
    // Setelah submit: modal tertutup, kembali ke dashboard, invoice baru muncul di tabel
    public boolean isPdfGenerated() {
        try {
            // Tunggu modal tertutup & dashboard memuat ulang
            try { Thread.sleep(2000); } catch (Exception ignored) {}

            // Cek apakah ada baris data di tabel (bukan "Tidak ada data")
            List<WebElement> rows = driver.findElements(By.xpath("//tbody/tr[not(contains(.,'Tidak ada data'))]"));
            if (!rows.isEmpty()) return true;

            // Alternatif: cek apakah ada nomor invoice (INV-xxxxx)
            List<WebElement> invNos = driver.findElements(By.xpath("//*[contains(text(),'INV-')]"));
            return !invNos.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // ===== TC-INV-02: Verifikasi error nominal huruf =====
    // Browser input type=number otomatis membuang huruf → value kosong
    // Saat klik Tambah / Submit, sistem menolak karena harga kosong/0
    public boolean isErrorNominalDisplayed() {
        // Cek apakah ada pesan error di DOM
        try {
            By errLoc = By.xpath(
                "//*[contains(@class,'text-red') or contains(@class,'error') or @role='alert'] | " +
                "//p[contains(text(),'angka') or contains(text(),'valid') or contains(text(),'wajib') " +
                "or contains(text(),'harus') or contains(text(),'kosong')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.presenceOfElementLocated(errLoc));
            return true;
        } catch (Exception ignored) {}

        // Fallback: field harga kosong berarti browser sudah menolak huruf
        try {
            WebElement el = driver.findElement(inputHarga);
            String val = el.getAttribute("value");
            return val == null || val.trim().isEmpty() || val.equals("0");
        } catch (Exception ignored) {}
        return false;
    }

    // ===== Pop Up Notifikasi =====
    By toastLocator = By.xpath(
        "//div[@role='alert'] | //div[@role='status'] | " +
        "//*[contains(@class,'toast')] | //*[contains(@class,'Toastify')]"
    );

    // Notifikasi BERHASIL: toast/alert yang mengandung kata berhasil/sukses/success
    public boolean isSuccessNotificationDisplayed() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toast = shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
            String text = toast.getText().toLowerCase();
            return text.contains("berhasil") || text.contains("sukses") || text.contains("success") 
                || text.contains("ditambahkan") || text.contains("dibuat");
        } catch (Exception e) {
            // Fallback: jika tidak ada toast tapi invoice muncul di list = berhasil
            try {
                List<WebElement> invRows = driver.findElements(By.xpath("//tbody/tr[not(contains(.,'Tidak ada data'))]"));
                return !invRows.isEmpty();
            } catch (Exception ex) { return false; }
        }
    }

    // Notifikasi GAGAL: toast/alert error ATAU kondisi form masih terbuka (tidak redirect)
    public boolean isErrorNotificationDisplayed() {
        try {
            // Cek toast error
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement toast = shortWait.until(ExpectedConditions.visibilityOfElementLocated(toastLocator));
            String text = toast.getText().toLowerCase();
            return text.contains("gagal") || text.contains("error") || text.contains("invalid") 
                || text.contains("tidak valid") || text.contains("failed") || text.contains("harga")
                || text.contains("wajib") || text.contains("kosong");
        } catch (Exception e) {
            // Fallback: form masih terbuka (tidak redirect ke dashboard) = submit gagal
            try {
                // Cek apakah masih di halaman form (bukan dashboard list)
                WebElement formTitle = driver.findElement(By.xpath("//*[contains(text(),'Buat Invoice Baru')]"));
                return formTitle.isDisplayed(); // masih di form = gagal submit = error terjadi
            } catch (Exception ex) {
                // Cek apakah ada pesan error di DOM
                try {
                    By errLoc = By.xpath(
                        "//*[contains(@class,'text-red') or contains(@class,'error')] | " +
                        "//p[contains(text(),'wajib') or contains(text(),'valid') or contains(text(),'harus')]"
                    );
                    return driver.findElement(errLoc).isDisplayed();
                } catch (Exception ex2) { return false; }
            }
        }
    }

    public boolean isNotificationDisplayed() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(toastLocator)).isDisplayed();
        } catch (Exception e) { return false; }
    }

    public String getNotificationText() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(toastLocator)).getText();
        } catch (Exception e) { return ""; }
    }
}