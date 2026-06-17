Feature: Alur Kalkulasi dan Penerbitan Invoice (Admin)

  Background:
    Given Admin berada di Halaman Login
    When Admin login menggunakan username "admin@klinikdrfanina.com" dan password "password123"
    And Admin diarahkan ke Dashboard dan menekan menu Manajemen Invoice

  Scenario: TC-INV-01 Membuat Invoice dengan data valid
    And Admin memilih pasien "testing" dan hewan "asd"
    And Admin mengisi tanggal invoice dan jatuh tempo
    And Admin menginput item layanan "Vaksin" dengan harga "100000", jumlah "1", PPN "11", dan diskon "0"
    Then Sistem menghitung total otomatis menjadi "100000"
    When Admin menekan tombol Buat Invoice
    Then Sistem menampilkan pop up notifikasi berhasil
    And Sistem menampilkan Halaman Detail PDF Invoice tanpa error

  Scenario: TC-INV-02 Membuat Invoice dengan input nominal huruf
    Given Admin berada di form buat Invoice
    When Admin menginput item layanan "Vaksin" dengan harga "abc", jumlah "1", PPN "0", dan diskon "0"
    And Admin menekan tombol Buat Invoice
    Then Sistem menampilkan pop up notifikasi gagal karena format nominal tidak valid
    And Sistem menolak dengan pesan error format nominal

  Scenario: TC-INV-03 Membuat Invoice dengan diskon 0% dan 100%
    Given Admin berada di form buat Invoice
    When Admin menginput item layanan "Konsultasi" dengan harga "50000", jumlah "1", PPN "0", dan diskon "0"
    Then Sistem menghitung total otomatis menjadi "50000"
    When Admin memasukkan diskon "100" lalu simpan
    Then Sistem menghitung total otomatis menjadi "50000"