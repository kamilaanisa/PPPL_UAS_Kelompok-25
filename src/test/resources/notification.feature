Feature: Manajemen dan Pemantauan Notifikasi Klinik drh Fanina

  Scenario: Pasien memverifikasi penerimaan teks notifikasi secara utuh
    Given User melakukan login dengan email "pasien@gmail.com" dan password "password123"
    And User berada di Dashboard
    When User mengklik ikon lonceng notifikasi di bilah navigasi utama
    Then Sistem menampilkan panel daftar pesan pengingat
    And Isi teks pesan pengingat harus berbunyi "Hari ini jadwal vaksinasi untuk ABC."

  Scenario: Admin memeriksa rincian detail log aktivitas notifikasi
    Given Admin melakukan login dengan email "admin@klinikdrfanina.com" dan password "password123"
    And Admin berada di halaman Log Notifikasi
    When Admin mengklik ikon detail info pada baris log aktivitas teratas
    Then Jendela modal Detail Notifikasi harus muncul di layar
    And Jendela modal menampilkan detail isi pesan secara komprehensif