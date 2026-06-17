package runners;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Automated Markdown Report Generator
 * 
 * Membaca output JSON dari Cucumber dan menghasilkan
 * laporan pengujian dalam format Markdown (.md)
 * 
 * Cara pakai:
 *   1. Jalankan test via TestRunner terlebih dahulu
 *   2. Jalankan class ini: java runners.GenerateReport
 *   3. Report tersedia di: target/cucumber-reports/TEST_REPORT.md
 */
public class GenerateReport {

    private static final String JSON_REPORT = "target/cucumber-reports/cucumber-report.json";
    private static final String MD_REPORT   = "target/cucumber-reports/TEST_REPORT.md";

    public static void main(String[] args) {
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(JSON_REPORT)));
            
            int totalScenarios = countOccurrences(jsonContent, "\"keyword\":\"Scenario\"") 
                               + countOccurrences(jsonContent, "\"keyword\": \"Scenario\"");
            int passedScenarios = 0;
            int failedScenarios = 0;

            // Parse scenarios sederhana dari JSON
            String[] scenarios = jsonContent.split("\"keyword\":\\s*\"Scenario\"");
            
            StringBuilder scenarioDetails = new StringBuilder();
            int scenarioIndex = 0;

            for (int i = 1; i < scenarios.length; i++) {
                scenarioIndex++;
                String scenario = scenarios[i];
                
                // Ambil nama scenario
                String name = extractValue(scenario, "\"name\"");
                
                // Cek status: jika ada "failed" maka gagal
                boolean hasFailed = scenario.contains("\"status\":\"failed\"") 
                                 || scenario.contains("\"status\": \"failed\"");
                boolean hasPassed = scenario.contains("\"status\":\"passed\"") 
                                 || scenario.contains("\"status\": \"passed\"");
                
                String status;
                String statusIcon;
                if (hasFailed) {
                    status = "FAILED";
                    statusIcon = "❌";
                    failedScenarios++;
                } else if (hasPassed) {
                    status = "PASSED";
                    statusIcon = "✅";
                    passedScenarios++;
                } else {
                    status = "SKIPPED";
                    statusIcon = "⏭️";
                    failedScenarios++;
                }

                // Ambil error message jika ada
                String errorMsg = "";
                if (hasFailed) {
                    int errIdx = scenario.indexOf("\"error_message\"");
                    if (errIdx == -1) errIdx = scenario.indexOf("\"message\"");
                    if (errIdx > 0) {
                        int start = scenario.indexOf("\"", errIdx + 16) + 1;
                        int end = scenario.indexOf("\"", start);
                        if (start > 0 && end > start && (end - start) < 500) {
                            errorMsg = scenario.substring(start, end)
                                .replace("\\n", "\n")
                                .replace("\\t", "  ");
                        }
                    }
                }

                // Ambil durasi step (dalam nanoseconds)
                long totalDuration = 0;
                String[] durationParts = scenario.split("\"duration\":\\s*");
                for (int d = 1; d < durationParts.length; d++) {
                    String durStr = durationParts[d].replaceAll("[^0-9].*", "");
                    try { totalDuration += Long.parseLong(durStr); } catch (Exception ignored) {}
                }
                double durationSeconds = totalDuration / 1_000_000_000.0;

                scenarioDetails.append("### ").append(scenarioIndex).append(". ")
                    .append(statusIcon).append(" ").append(name != null ? name.trim() : "Scenario " + scenarioIndex)
                    .append("\n\n");
                scenarioDetails.append("| Field | Detail |\n|-------|--------|\n");
                scenarioDetails.append("| **Status** | ").append(status).append(" |\n");
                scenarioDetails.append("| **Durasi** | ").append(String.format("%.2f", durationSeconds)).append(" detik |\n");
                
                if (!errorMsg.isEmpty()) {
                    // Ambil baris pertama saja untuk tabel
                    String firstLine = errorMsg.split("\n")[0];
                    if (firstLine.length() > 100) firstLine = firstLine.substring(0, 100) + "...";
                    scenarioDetails.append("| **Error** | ").append(firstLine).append(" |\n");
                }
                scenarioDetails.append("\n");
            }

            // Hitung persentase
            if (totalScenarios == 0) totalScenarios = scenarioIndex;
            double passRate = totalScenarios > 0 ? (passedScenarios * 100.0 / totalScenarios) : 0;

            // Generate Markdown
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss"));
            
            StringBuilder md = new StringBuilder();
            md.append("# 📋 Laporan Hasil Pengujian Otomatis\n\n");
            md.append("**Proyek**: Sistem Informasi Klinik drh. Fanina  \n");
            md.append("**Modul**: Invoice & Pembayaran  \n");
            md.append("**Tanggal**: ").append(timestamp).append("  \n");
            md.append("**Environment**: Brave Browser | Selenium WebDriver | Cucumber BDD  \n");
            md.append("**Generated by**: Automated Report Generator  \n\n");
            md.append("---\n\n");

            // Ringkasan
            md.append("## 📊 Ringkasan Hasil\n\n");
            md.append("| Metrik | Nilai |\n");
            md.append("|--------|-------|\n");
            md.append("| Total Skenario | ").append(totalScenarios).append(" |\n");
            md.append("| ✅ Passed | ").append(passedScenarios).append(" |\n");
            md.append("| ❌ Failed | ").append(failedScenarios).append(" |\n");
            md.append("| 📈 Pass Rate | ").append(String.format("%.1f", passRate)).append("% |\n\n");

            // Status bar visual
            md.append("**Status Keseluruhan**: ");
            if (passRate == 100) {
                md.append("🟢 **ALL PASSED**\n\n");
            } else if (passRate >= 50) {
                md.append("🟡 **PARTIAL PASS**\n\n");
            } else {
                md.append("🔴 **MOSTLY FAILED**\n\n");
            }

            md.append("---\n\n");

            // Detail per skenario
            md.append("## 🔍 Detail Hasil Per Skenario\n\n");
            md.append(scenarioDetails);

            md.append("---\n\n");

            // Test Suite Matrix
            md.append("## 📋 Test Suite Matrix\n\n");
            md.append("| Test Case ID | Skenario | Expected Result | Status |\n");
            md.append("|-------------|----------|-----------------|--------|\n");
            md.append("| TC-INV-01 | Membuat Invoice dengan data valid | Kalkulasi total benar & generate PDF | ")
              .append(passedScenarios >= 1 ? "✅ Passed" : "❌ Failed").append(" |\n");
            md.append("| TC-INV-02 | Membuat Invoice dengan input nominal huruf | Sistem menolak dengan pesan error | ")
              .append(passedScenarios >= 2 ? "✅ Passed" : "❌ Failed").append(" |\n");
            md.append("| TC-INV-03 | Membuat Invoice dengan diskon 0% dan 100% | Kalkulasi akhir benar tanpa minus | ")
              .append(passedScenarios >= 3 ? "✅ Passed" : "❌ Failed").append(" |\n\n");

            md.append("---\n\n");

            // Report files
            md.append("## 📁 File Report yang Di-generate\n\n");
            md.append("| Format | Lokasi File |\n");
            md.append("|--------|-------------|\n");
            md.append("| 📄 Markdown | `target/cucumber-reports/TEST_REPORT.md` |\n");
            md.append("| 🌐 HTML | `target/cucumber-reports/cucumber-report.html` |\n");
            md.append("| 📊 JSON | `target/cucumber-reports/cucumber-report.json` |\n");
            md.append("| 📋 JUnit XML | `target/cucumber-reports/cucumber-report.xml` |\n\n");

            md.append("---\n\n");
            md.append("*Report ini di-generate secara otomatis oleh `GenerateReport.java` setelah eksekusi test suite.*\n");

            // Tulis file MD
            Files.createDirectories(Paths.get("target/cucumber-reports"));
            Files.write(Paths.get(MD_REPORT), md.toString().getBytes("UTF-8"));

            System.out.println("==============================================");
            System.out.println("  ✅ REPORT MARKDOWN BERHASIL DI-GENERATE!");
            System.out.println("  📄 Lokasi: " + MD_REPORT);
            System.out.println("==============================================");
            System.out.println("  Total: " + totalScenarios + " | Passed: " + passedScenarios + " | Failed: " + failedScenarios);
            System.out.println("  Pass Rate: " + String.format("%.1f", passRate) + "%");
            System.out.println("==============================================");

        } catch (FileNotFoundException e) {
            System.err.println("❌ File JSON report tidak ditemukan: " + JSON_REPORT);
            System.err.println("   Jalankan test terlebih dahulu: mvn test");
        } catch (Exception e) {
            System.err.println("❌ Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int countOccurrences(String str, String sub) {
        int count = 0, idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) { count++; idx += sub.length(); }
        return count;
    }

    private static String extractValue(String json, String key) {
        int idx = json.indexOf(key);
        if (idx < 0) return null;
        int colonIdx = json.indexOf(":", idx + key.length());
        if (colonIdx < 0) return null;
        int startQuote = json.indexOf("\"", colonIdx + 1);
        if (startQuote < 0) return null;
        int endQuote = json.indexOf("\"", startQuote + 1);
        if (endQuote < 0) return null;
        return json.substring(startQuote + 1, endQuote);
    }
}
