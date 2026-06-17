### Laporan Bug Pengujian (Tester: Kamila - Modul Notifikasi)

**BUG-01: Jeda Waktu (Delay) Render UI pada Dashboard dan Menu Navigasi**
* **Severity:** Minor
* **Halaman/Fitur:** Dashboard User & Log Notifikasi Admin
* **Langkah Reproduksi:**
    1. Login menggunakan kredensial Pasien atau Admin.
    2. Tunggu hingga dialihkan ke halaman Dashboard.
    3. Amati waktu kemunculan elemen navigasi (Ikon Lonceng / Menu Sidebar).
* **Expected Result:** Elemen UI navigasi langsung siap dirender dan dapat diklik (clickable) seketika setelah proses otentikasi berhasil.
* **Actual Result:** Terdapat jeda (delay) render dari sisi client (Vercel) sekitar 2-3 detik yang menyebabkan elemen tidak langsung interaktif, sehingga berpotensi menyebabkan *timeout* pada pengujian otomatis jika tidak diberikan eksplisit *wait*.