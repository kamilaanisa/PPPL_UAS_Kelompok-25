# End-to-End (E2E) Testing - Praktik Dokter Hewan Fanina

Repositori ini berisi sekumpulan skenario pengujian otomatis (Automated E2E Testing) untuk memastikan kualitas dan fungsionalitas sistem berjalan dengan optimal.

---

## 1. Penjelasan Singkat SUT (System Under Test)
**System Under Test (SUT)** dalam proyek ini adalah **Sistem Informasi Manajemen Klinik Praktik Dokter Hewan Fanina**. Aplikasi berbasis web ini dirancang untuk mendigitalisasi operasional klinik hewan, mulai dari manajemen autentikasi pengguna, penyediaan pusat informasi (FAQ) publik, kalkulasi otomatis biaya tindakan dan obat (*Invoice*), hingga sistem pengingat otomatis jadwal vaksinasi pasien melalui integrasi WhatsApp Gateway (Evolution API). Pengujian E2E ini difokuskan pada simulasi alur pengguna nyata (*User Journey*) guna memvalidasi fungsionalitas *Front-End* (Next.js) dan keandalan logika *Back-End* (Laravel).

---

## 2. Penjelasan Singkat Test Suite
*Test Suite* didesain secara komprehensif mencakup 4 modul utama dengan total 13 *Test Cases* untuk menguji skenario positif (*Normal Test*) maupun skenario negatif (*Negative Test*) menggunakan teknik *Boundary Value Analysis* (BVA) dan *Equivalence Partitioning* (EP).

| Test Case ID | Modul / Fitur | Test Scenario | Pre-Condition | Test Steps | Expected Result |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-LOG-01** | Login & Profile | Login dengan kredensial valid | Berada di halaman Login | 1. Input email valid.<br>2. Input password valid.<br>3. Klik Login | Sistem mengarahkan ke Dashboard |
| **TC-LOG-02** | Login & Profile | Login dengan password salah | Berada di halaman Login | 1. Input email valid.<br>2. Input password SALAH.<br>3. Klik Login | Sistem menampilkan pesan error kredensial tidak cocok |
| **TC-LOG-03** | Login & Profile | Update Profil (Ganti Nama) | Login sebagai User dan masuk ke menu Profil | 1. Klik Edit Profil.<br>2. Ubah Nama.<br>3. Klik Simpan | Sistem menyimpan perubahan dan menampilkan notifikasi sukses |
| **TC-LOG-04** | Login & Profile | Update Password batas bawah (< 8 karakter) | Login sebagai User dan masuk ke menu Profil | 1. Masukkan password baru 7 karakter.<br>2. Klik Simpan | Sistem menolak dan menampilkan pesan error minimal 8 karakter |
| **TC-FAQ-01** | FAQ | Admin tambah FAQ valid | Login sebagai Admin dan masuk menu FAQ | 1. Isi Pertanyaan.<br>2. Isi Jawaban.<br>3. Klik Simpan | Data FAQ tersimpan di database dan tampil di tabel |
| **TC-FAQ-02** | FAQ | Admin tambah FAQ dengan form kosong | Berada di menu form tambah FAQ | 1. Kosongkan kolom Pertanyaan.<br>2. Klik Simpan | Sistem menampilkan pesan error validasi |
| **TC-FAQ-03** | FAQ | User mencari FAQ dengan kata kunci yang ada | User berada di halaman pencarian FAQ | 1. Ketik kata kunci valid di kolom pencarian.<br>2. Tekan Enter | Sistem memunculkan dropdown jawaban yang relevan |
| **TC-INV-01** | Invoice | Membuat Invoice dengan data valid | Login sebagai Admin dan masuk menu Invoice | 1. Pilih Pemilik & Hewan.<br>2. Isi tanggal invoice dan jatuh tempo.<br>3. Isi Layanan & Harga valid.<br>4. Klik Buat Invoice | Sistem mengkalkulasi total dengan benar dan generate PDF |
| **TC-INV-02** | Invoice | Membuat Invoice dengan input nominal huruf | Login sebagai Admin dan masuk menu Invoice | 1. Masukkan huruf pada kolom Harga.<br>2. Klik Buat Invoice | Sistem menolak dengan pesan error format nominal |
| **TC-INV-03** | Invoice | Membuat Invoice dengan diskon 0% dan 100% | Login sebagai Admin dan masuk menu Invoice | 1. Masukkan diskon 0% lalu simpan.<br>2. Masukkan diskon 100% lalu simpan | Sistem dapat mengkalkulasi akhir dengan benar tanpa minus |
| **TC-NOT-01** | Notifikasi | Admin trigger reminder notifikasi | Login Admin dan di menu Reminder | 1. Pilih jadwal hewan.<br>2. Klik Kirim | Sistem mengeksekusi pengiriman dan status menjadi Terkirim |
| **TC-NOT-02** | Notifikasi | User verifikasi penerimaan notifikasi | Login sebagai User | 1. Masuk Dashboard.<br>2. Klik menu Notifikasi.<br>3. Klik pesan | Sistem menampilkan detail notifikasi teks secara utuh |
| **TC-NOT-03** | Notifikasi | Cek log notifikasi gagal (nomor kosong) | Login Admin | 1. Trigger notifikasi untuk pasien tanpa nomor HP.<br>2. Cek Log | Sistem mencatat status failed di log notifikasi |

---

## 3. Pembagian Tugas Kelompok
Setiap anggota tim bertanggung jawab penuh terhadap perancangan skenario, penulisan skrip otomasi, hingga analisis hasil pengujian pada modul berikut:

| Anggota Tim | Fitur/Halaman yang Dipegang | Cakupan Pengujian |
| :--- | :--- | :--- |
| **Lala** | Halaman Login & Manajemen Profil | Validasi format email, verifikasi kredensial salah, batas minimal karakter kata sandi (BVA), pengecekan field wajib kosong. |
| **Kamila** | Halaman Kotak Masuk Notifikasi & Log Notifikasi Admin | Validasi pemicu (trigger) pengiriman pesan, verifikasi tampilan teks pesan secara utuh, pengujian nomor kontak kosong/tidak valid pada log (failed status). |
| **Rakai** | Halaman FAQ Publik & Manajemen FAQ Admin | Filter kata kunci pencarian, pengujian form input tanya jawab kosong, verifikasi fungsi interaksi dropdown hasil pencarian. |
| **Falah** | Halaman Pembuatan Invoice & Perhitungan Biaya | Validasi rumus kalkulasi otomatis (Layanan, Obat, PPN), batasan range input diskon 0% hingga 100% (BVA), validasi tipe data format nominal harga (EP - huruf/karakter khusus). |

---

## 4. Struktur Repositori (Repository Structure)
Framework pengujian otomatis ini dibangun menggunakan **Java**, **Selenium WebDriver**, dan **Cucumber (BDD)** dengan menerapkan arsitektur *Page Object Model (POM)* untuk memisahkan logika elemen UI dengan langkah pengujian.

```text
├── src/
│   └── test/
│       ├── java/
│       │   ├── pages/             # Kelas Page Object (Web Element Locators & Actions)
│       │   ├── stepDefinitions/   # Implementasi kode Java dari langkah Gherkin
│       │   └── TestRunner.java    # Konfigurasi eksekusi dan plugin laporan Cucumber
│       └── resources/
│           └── features/          # Berkas berkstur .feature berisi skenario Gherkin BDD
├── target/                        # Hasil build dan laporan pengujian otomatis (Cluecumber Report)
└── pom.xml                        # Manajemen dependensi pustaka Maven (Selenium, Cucumber, JUnit)
