# 🐞 Bug Report — Modul Invoice

**Proyek**: Sistem Informasi Klinik drh. Fanina  
**URL**: https://compro-drhfanina-pad1.vercel.app  
**Tanggal Pengujian**: 17 Juni 2026
**Environment**: Brave Browser v1.x | ChromeDriver | Windows 11

---

## Bug #1 — Tidak Ada Pesan Error Saat Input Huruf di Kolom Harga

| Field | Detail |
|-------|--------|
| **Bug ID** | BUG-INV-001 |
| **Modul** | Invoice — Form Buat Invoice Baru |
| **Severity** | 🟡 Medium |
| **Priority** | High |
| **Status** | Open |
| **Test Case Terkait** | TC-INV-02 |

### Deskripsi
Ketika admin memasukkan huruf (contoh: `abc`) pada kolom **Harga Satuan** (yang bertipe `input type="number"`), browser secara diam-diam menolak input tersebut. Kolom menjadi kosong tanpa menampilkan pesan error yang informatif kepada pengguna.

### Langkah Reproduksi
1. Login sebagai Admin
2. Masuk menu Invoice → Klik **+ Buat Invoice**
3. Pilih Pemilik dan Hewan
4. Pada kolom **Harga Satuan**, ketik `abc`
5. Klik **+ Tambah** atau **Buat Invoice**

### Expected Result
Sistem menampilkan **pesan error yang jelas** seperti: *"Harga harus berupa angka"* atau *"Format nominal tidak valid"*

### Actual Result
- Kolom Harga Satuan menjadi **kosong** (browser membuang huruf secara silent)
- **Tidak ada pesan error visual** yang muncul di UI
- Jika klik Tambah, item tidak ditambahkan tanpa penjelasan

### Dampak
User tidak mendapat feedback yang jelas mengapa input ditolak. Ini melanggar prinsip UX *"Visibility of System Status"* (Nielsen's Heuristic #1).

### Rekomendasi
Tambahkan validasi frontend yang menampilkan pesan error inline di bawah field Harga ketika input bukan angka, contoh:
```
⚠ Harga harus berupa angka yang valid
```

---

## Bug #2 — Tanggal Invoice Kosong Tidak Ada Validasi Frontend

| Field | Detail |
|-------|--------|
| **Bug ID** | BUG-INV-002 |
| **Modul** | Invoice — Form Buat Invoice Baru |
| **Severity** | 🔴 High |
| **Priority** | High |
| **Status** | Open |
| **Test Case Terkait** | TC-INV-01 |

### Deskripsi
Field **Tanggal Invoice** dan **Jatuh Tempo** wajib diisi untuk membuat invoice. Namun jika kedua field tersebut dikosongkan dan admin menekan "Buat Invoice", **tidak ada pesan error visual** yang muncul di form. Sistem hanya mengembalikan error HTTP 422 dari backend yang tidak ditampilkan kepada user.

### Langkah Reproduksi
1. Login sebagai Admin
2. Masuk menu Invoice → Klik **+ Buat Invoice**
3. Pilih Pemilik dan Hewan
4. **Biarkan Tanggal Invoice dan Jatuh Tempo kosong**
5. Tambah item lalu klik **Buat Invoice**

### Expected Result
Pesan error muncul di bawah field tanggal: *"Tanggal invoice wajib diisi"*

### Actual Result
- Tidak ada pesan error di UI
- Invoice gagal disimpan (HTTP 422 Unprocessable Entity)
- User tidak tahu mengapa invoice gagal dibuat

### Dampak
User experience buruk karena tidak ada feedback. User menganggap sistem error padahal hanya tanggal yang kosong.

### Rekomendasi
Tambahkan validasi `required` dengan pesan error inline pada kedua field tanggal.

---

## Ringkasan Bug

| Bug ID | Severity | Deskripsi Singkat | Status |
|--------|----------|-------------------|--------|
| BUG-INV-001 | 🟡 Medium | Tidak ada error message saat input huruf di Harga | Open |
| BUG-INV-002 | 🔴 High | Tanggal kosong tidak ada validasi frontend | Open |

**Total Bug Ditemukan**: 2

---

*Dokumen ini dibuat berdasarkan hasil pengujian automation Selenium + Cucumber pada modul Invoice.*
