Feature: Manajemen dan Pencarian FAQ

  Scenario: Admin menambahkan FAQ baru, user mencarinya, dan admin menguji validasi form kosong
    # --- FLOW 1: ADMIN TAMBAH DATA ---
    Given Admin berada di halaman Login
    When Admin memasukkan username dan password yang valid
    And Admin menekan tombol login
    Then Admin berhasil masuk ke dashboard admin
    Given Admin berada di halaman Manajemen FAQ
    When Admin menambahkan pertanyaan baru "Bagaimana cara reset password?"
    And Admin menambahkan jawaban baru "Klik menu lupa password pada halaman login."
    And Admin menekan tombol simpan
    Then Data FAQ baru berhasil ditambahkan

    # --- FLOW 2: USER MENCARI DATA ---
    Given User berada di Landing Page
    Then User dapat melihat navigasi menuju halaman FAQ atau Pusat Bantuan
    Given User berada di halaman FAQ atau Pusat Bantuan
    When User mengetik kata kunci "reset password" pada kolom pencarian
    Then Sistem menampilkan FAQ yang relevan dengan kata kunci
    When User mengklik hasil FAQ "Bagaimana cara reset password?"
    Then Sistem menampilkan detail jawaban FAQ
    And User dapat membaca jawaban "Klik menu lupa password pada halaman login."

    # --- FLOW 3: KEMBALI KE ADMIN & UJI FORM KOSONG ---
    Given Admin kembali ke halaman Manajemen FAQ
    When Admin mengosongkan kolom Pertanyaan
    And Admin menekan tombol simpan
    Then Sistem menampilkan pesan error validasi