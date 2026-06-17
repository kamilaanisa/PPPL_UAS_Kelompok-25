package stepDefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.time.Duration;

public class Hooks {
    private static WebDriver driver;

    @Before
    public void setup() {
        // 1. Konfigurasi menggunakan Browser Brave
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/usr/bin/brave");

        driver = new ChromeDriver(options);

        // 2. Atur Windows & Timeout
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // Catatan: driver.get() tidak ditaruh di sini jika halaman awal
        // antara Admin Login dan User Landing Page berbeda.
        // Buka URL sebaiknya dilakukan di dalam masing-masing `@Given` step.
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // Method ini digunakan oleh class Steps untuk mengambil driver yang aktif
    public static WebDriver getDriver() {
        return driver;
    }
}