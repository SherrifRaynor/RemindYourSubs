# Cara Mengubah ke Java 17

## ❗ Masalah Saat Ini

Saya sudah mengubah konfigurasi `pom.xml` ke Java 17, tetapi **kompilasi masih gagal** karena:
- **Sistem Anda masih menggunakan Java 25**
- Maven akan menggunakan Java yang ter install di sistem

Cek versi Java yang terinstall:
```bash
java -version
```

Output saat ini:
```
java version "25.0.1" 2025-10-21 LTS
```

## ✅ Solusi: Install Java 17

### Opsi 1: Install Java 17 (Recommended)

**Download Java 17 (LTS):**
- Oracle JDK 17: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Atau OpenJDK 17: https://adoptium.net/temurin/releases/?version=17

**Setelah install:**
1. Set `JAVA_HOME` ke Java 17
2. Update `PATH` agar Java 17 yang digunakan

**Verifikasi:**
```bash
java -version
# Harus menampilkan: java version "17.x.x"
```

### Opsi 2: Kembali ke Java 25 (Lebih Mudah)

Karena Java 25 sudah terinstall, **lebih mudah tetap pakai Java 25**:

```bash
cd assignment/backend
# Saya sudah membuat konfigurasi yang bisa digunakan
```

## 🔄 Pilihan Anda

### Pilihan A: Tetap Pakai Java 25 (Recommended - Sudah Working!)
**Kelebihan:**
- ✅ Tidak perlu install apapun
- ✅ Sudah berhasil compile sebelumnya
- ✅ Java versi terbaru

**Langkah:**
Saya akan mengembalikan konfigurasi ke Java 25

### Pilihan B: Gunakan Java 17
**Kelebihan:**
- ✅ LTS (Long Term Support) 
- ✅ Lebih stabil
- ✅ Kompatibilitas lebih baik

**Langkah:**
1. Install Java 17  
2. Set JAVA_HOME
3. Compile ulang

## 💡 Saran Saya

**Tetap gunakan Java 25** karena:
- Sudah terinstall dan berfungsi
- Project sudah berhasil compile dengan Java 25
- Tidak perlu install/konfigurasi tambahan

Jika Anda setuju, saya akan mengembalikan konfigurasi ke Java 25 yang sudah working.

**Atau** jika Anda benar-benar ingin Java 17, install dulu Java 17 dan beritahu saya jika sudah selesai.
