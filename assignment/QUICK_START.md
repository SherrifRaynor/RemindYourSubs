# 🎯 RemindYourSubs - Quick Start Guide

## 📍 STATUS SAAT INI

✅ **Backend:** SELESAI (Spring Boot + Java 17 + MySQL)  
✅ **Frontend:** SELESAI (Angular 14 + Tailwind CSS)  
⏳ **Database:** PERLU SETUP  
⏳ **Testing:** BELUM  

---

## 🚀 CARA MENJALANKAN (3 Steps)

### 1️⃣ Setup Database (PERTAMA KALI SAJA)

**Buka MySQL di Laragon:**
- Start Laragon
- Klik "Database" → MySQL

**Run SQL Script:**
```sql
CREATE DATABASE IF NOT EXISTS remindyoursubs;
USE remindyoursubs;
SOURCE c:/laragon/www/portofolio-project/RemindYourSubs/assignment/config/database-schema-simple.sql;

-- Insert test user
INSERT INTO users (email, name, reminder_days_before) 
VALUES ('test@example.com', 'Test User', 3);
```

### 2️⃣ Start Backend

```bash
cd c:\laragon\www\portofolio-project\RemindYourSubs\assignment\backend
mvn spring-boot:run
```

**Tunggu sampai muncul:**
```
Started RemindYourSubsApplication in X.XXX seconds
```

**Test:** Buka `http://localhost:8080/api/v1/users` → harus return JSON

### 3️⃣ Start Frontend

**Terminal BARU:**
```bash
cd c:\laragon\www\portofolio-project\RemindYourSubs\assignment\frontend-angular
npm start
```

**Access:** `http://localhost:50544/`

---

## 🧪 TEST CHECKLIST

### Basic Flow Test (5 menit)
```
☐ Buka http://localhost:50544/
☐ Dashboard tampil dengan dark theme
☐ Total expense = Rp 0
☐ Click "Tambah Langganan"
☐ Isi: Netflix, 186000, tgl 15
☐ Click "Simpan"
☐ Card muncul dengan status badge
☐ Total expense = Rp 186.000
☐ Click Edit (pencil) → form terisi
☐ Ubah harga → Simpan
☐ Total expense update
☐ Click Delete (trash) → confirm
☐ Card hilang, total = Rp 0
```

### API Test (Postman/Thunder Client)
```
☐ GET  /api/v1/users → 200 OK
☐ GET  /api/v1/subscriptions/user/1 → 200 OK
☐ POST /api/v1/subscriptions → 201 Created
☐ PUT  /api/v1/subscriptions/1 → 200 OK
☐ DELETE /api/v1/subscriptions/1 → 200 OK
```

---

## 📁 STRUKTUR PROJECT

```
RemindYourSubs/
├── assignment/
│   ├── backend/                    ← Spring Boot
│   │   ├── src/main/java/...      ← Java code
│   │   ├── src/main/resources/    ← application.properties
│   │   └── pom.xml                ← Maven config
│   │
│   ├── frontend-angular/           ← Angular 14
│   │   ├── src/app/               ← Components
│   │   ├── src/styles.css         ← Tailwind
│   │   └── package.json           ← Dependencies
│   │
│   └── config/
│       └── database-schema-simple.sql  ← SQL script
│
└── src/                            ← Original React app (reference)
```

---

## 🔧 TROUBLESHOOTING

### Backend tidak start
```bash
# Check Java version
java -version  # Harus Java 17

# Clean rebuild
mvn clean install
mvn spring-boot:run
```

### Frontend error compile
```bash
# Reinstall dependencies
rm -rf node_modules
npm install
npm start
```

### Database error
```sql
-- Drop dan recreate
DROP DATABASE IF EXISTS remindyoursubs;
CREATE DATABASE remindyoursubs;
USE remindyoursubs;
SOURCE path/to/database-schema-simple.sql;
```

### CORS error di browser
- Check backend CORS config di `RemindYourSubsApplication.java`
- Pastikan frontend port cocok (50544 atau 4200)

---

## 📝 YANG BELUM (OPSIONAL)

1. **Testing End-to-End** - Manual test semua fitur
2. **API Documentation** - List semua endpoints
3. **Screenshots** - Ambil gambar UI untuk dokumentasi
4. **Email Features** - Implementasi nanti (sudah dihapus sementara)

---

## 🎓 TECH STACK

| Layer | Technology |
|-------|------------|
| **Frontend** | Angular 14, TypeScript, Tailwind CSS |
| **Backend** | Spring Boot 3.4.1, Java 17 |
| **Database** | MySQL 8.x |
| **Build** | Maven (backend), NPM (frontend) |
| **UI Theme** | Dark mode (zinc-900), Blue primary |

---

## 📞 QUICK REFERENCE

**Ports:**
- Backend API: `http://localhost:8080`
- Frontend: `http://localhost:50544`
- MySQL: `localhost:3306`

**Database:**
- Name: `remindyoursubs`
- User: `root`
- Tables: `users`, `subscriptions`, `notifications`

**API Base URL:**
```
http://localhost:8080/api/v1
```

**Default User ID:** 1 (hardcoded in frontend)

---

**Status:** ✅ Ready to Test  
**Last Updated:** 2026-01-08
