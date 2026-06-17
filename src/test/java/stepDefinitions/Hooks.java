package stepDefinitions;

import io.cucumber.java.AfterAll;
import runners.GenerateReport;

/**
 * Cucumber Hooks untuk auto-generate report.
 * 
 * @AfterAll dijalankan SEKALI setelah SEMUA skenario selesai.
 * Otomatis memanggil GenerateReport untuk membuat TEST_REPORT.md
 */
public class Hooks {

    @AfterAll
    public static void generateReport() {
        System.out.println("\n========================================");
        System.out.println("Auto-generating Markdown report...");
        System.out.println("========================================\n");

        // Tunggu sebentar agar file JSON selesai ditulis
        try { Thread.sleep(1000); } catch (Exception ignored) {}

        // Panggil GenerateReport.main() secara otomatis
        GenerateReport.main(new String[]{});
    }
}
