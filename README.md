# API Backend - POS System

| Nama | Kelas |
|--------|---------|
| Dafa Adrenalin Pratama  | XI RPL A  | 

Backend API untuk sistem Point of Sale (POS) dengan role-based access control menggunakan Spring Boot dan PostgreSQL.

## 📋 Deskripsi

Sistem backend API untuk POS dengan 2 role:
- **Kasir**: Dapat melakukan transaksi, melihat produk dan kategori
- **Admin**: Memiliki akses penuh untuk CRUD produk, kategori, user management, dan melihat semua transaksi

## 🚀 Teknologi

- Java 17
- Spring Boot 3.2.5
- PostgreSQL
- Maven 3.6+
- Swagger/OpenAPI untuk dokumentasi API
- Spring Security untuk session-based authentication
- Lombok untuk reduce boilerplate code
- JPA/Hibernate untuk ORM

## 📦 Struktur Database

### Tables:
1. **users** - Menyimpan data user (Kasir & Admin)
   - id, username, password, name, role, active, created_at, updated_at
2. **categories** - Kategori produk
   - id, name, description, created_at, updated_at
3. **products** - Data produk
   - id, name, description, price, stock, category_id, created_at, updated_at
4. **transactions** - Header transaksi
   - id, user_id, total_amount, transaction_date, created_at, updated_at
5. **transaction_items** - Detail item transaksi
   - id, transaction_id, product_id, quantity, price, subtotal

## 🏗️ Struktur Project

```
jp-api-backend-java/
├── pom.xml                          # Maven dependencies
├── README.md
├── docker-compose.yml               # Docker setup (opsional)
└── src/
    ├── main/
    │   ├── java/com/example/jpapi/
    │   │   ├── JpApiBackendApplication.java    # Main class
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java         # Spring Security config
    │   │   │   └── SwaggerConfig.java          # Swagger/OpenAPI config
    │   │   ├── controller/
    │   │   │   ├── AuthController.java         # Login/Register/Logout
    │   │   │   ├── UserController.java         # User management
    │   │   │   ├── CategoryController.java     # Category CRUD
    │   │   │   ├── ProductController.java      # Product CRUD
    │   │   │   └── TransactionController.java  # Transaction management
    │   │   ├── service/
    │   │   │   ├── AuthService.java
    │   │   │   ├── UserService.java
    │   │   │   ├── CategoryService.java
    │   │   │   ├── ProductService.java
    │   │   │   └── TransactionService.java
    │   │   ├── repository/
    │   │   │   ├── UserRepository.java
    │   │   │   ├── CategoryRepository.java
    │   │   │   ├── ProductRepository.java
    │   │   │   └── TransactionRepository.java
    │   │   ├── model/
    │   │   │   ├── User.java
    │   │   │   ├── Category.java
    │   │   │   ├── Product.java
    │   │   │   ├── Transaction.java
    │   │   │   └── TransactionItem.java
    │   │   ├── dto/
    │   │   │   ├── ApiResponse.java
    │   │   │   ├── LoginRequest.java
    │   │   │   ├── RegisterRequest.java
    │   │   │   ├── UserResponse.java
    │   │   │   └── ... (other DTOs)
    │   │   └── exception/
    │   │       └── GlobalExceptionHandler.java
    │   └── resources/
    │       ├── application.properties           # Database & app config
    │       └── application-test.properties      # Test config
    └── test/
        └── java/                                # Unit tests
```

## 🔧 Instalasi

### Prerequisites
- Java 17 atau lebih tinggi
- PostgreSQL 12+
- Maven 3.6+

### Setup Database

1. Install PostgreSQL (macOS):
```bash
brew install postgresql@15
brew services start postgresql@15
```

2. Buat database PostgreSQL:
```bash
# Login ke PostgreSQL
psql -U postgres

# Buat database
CREATE DATABASE pos_db;
\q
```

3. Update konfigurasi database di `src/main/resources/application.properties` (jika perlu):
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pos_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Run Application

```bash
# Download dependencies dan build
mvn clean install

# Jalankan aplikasi
mvn spring-boot:run
```

Aplikasi akan berjalan di `http://localhost:8080`

Hibernate akan otomatis membuat tabel-tabel yang diperlukan saat pertama kali dijalankan (`ddl-auto=update`).

## 📚 API Documentation

Setelah aplikasi berjalan, akses Swagger UI di:
```
http://localhost:8080/swagger-ui.html
```

## 🔐 Authentication

Sistem menggunakan **Session-based authentication**. 

### Flow Authentication:
1. Login dengan endpoint `/api/auth/login`
2. Session akan disimpan dan cookie `JSESSIONID` akan dikirim
3. Gunakan cookie tersebut untuk request selanjutnya
4. Logout dengan endpoint `/api/auth/logout`

### Catatan Testing:
- **Swagger UI**: Kadang tidak menyimpan session cookie dengan baik, gunakan Postman atau curl untuk testing yang lebih reliable
- **Postman**: Enable "Automatically follow redirects" dan simpan cookies
- **curl**: Gunakan `-c cookies.txt` untuk menyimpan dan `-b cookies.txt` untuk menggunakan cookies

## 📝 API Endpoints

### Authentication (Public Access)
- `POST /api/auth/register` - Register user baru (Kasir/Admin)
- `POST /api/auth/login` - Login
- `POST /api/auth/logout` - Logout
- `GET /api/auth/profile` - Get profile user yang login

### Users (Admin Only)
- `GET /api/users` - Get semua user/profil
- `PUT /api/users/{id}/deactivate` - Deactivate user kasir (tidak bisa deactivate admin atau diri sendiri)

### Categories (Read: All, Write: Admin Only)
- `GET /api/categories` - Get semua kategori
- `POST /api/categories` - Create kategori (Admin)
- `PUT /api/categories/{id}` - Update kategori (Admin)
- `DELETE /api/categories/{id}` - Delete kategori (Admin)

### Products (Read: All, Write: Admin Only)
- `GET /api/products` - Get semua produk
- `GET /api/products/{id}` - Get produk by ID
- `POST /api/products` - Create produk (Admin)
- `PUT /api/products/{id}` - Update produk (Admin)
- `DELETE /api/products/{id}` - Delete produk (Admin)

### Transactions (Kasir & Admin)
- `GET /api/transactions` - Get semua transaksi
- `GET /api/transactions/{id}` - Get transaksi by ID
- `POST /api/transactions` - Create transaksi baru
- `PUT /api/transactions/{id}` - Update transaksi (tambah item)

## 🧪 Testing dengan curl

### 1. Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "admin123",
    "name": "Admin Satu",
    "role": "ADMIN"
  }'
```

### 2. Login (simpan session)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin1",
    "password": "admin123"
  }' \
  -c cookies.txt
```

### 3. Get All Users (gunakan session)
```bash
curl -X GET http://localhost:8080/api/users \
  -b cookies.txt
```

### 4. Deactivate User Kasir
```bash
curl -X PUT http://localhost:8080/api/users/1/deactivate \
  -b cookies.txt
```

### 5. Create Category
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "name": "Minuman",
    "description": "Kategori minuman"
  }'
```

### 6. Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "name": "Kopi Susu",
    "description": "Kopi susu segar",
    "price": 15000,
    "stock": 100,
    "categoryId": 1
  }'
```

### 7. Create Transaction
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

## 🔒 Security & Business Rules

### Password & Authentication
- Password di-encrypt menggunakan BCrypt
- Session timeout: 30 menit
- CSRF protection disabled (untuk development/testing, enable di production)
- Maximum sessions per user: 1

### User Management Rules
- ✅ Admin dapat melihat semua user (`GET /api/users`)
- ✅ Admin dapat deactivate user dengan role **KASIR**
- ❌ Admin **tidak bisa** deactivate diri sendiri
- ❌ Admin **tidak bisa** deactivate admin lain
- User yang di-deactivate tidak bisa login

### Transaction Rules
- Stock produk otomatis berkurang saat transaksi dibuat
- Total amount dihitung otomatis dari (price × quantity) semua item
- Validasi stock tersedia sebelum transaksi
- User ID transaksi diambil dari session (user yang login)

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Testing dengan Postman
1. Import collection atau buat request manual
2. **Enable** "Automatically follow redirects" di Settings
3. Login terlebih dahulu untuk mendapatkan session cookie
4. Cookie akan otomatis disimpan dan digunakan untuk request selanjutnya

## 📈 Development

### Build untuk Production
```bash
mvn clean package
java -jar target/jp-api-backend-1.0.0.jar
```

### Environment Profiles
- **Default** (`application.properties`): Development dengan PostgreSQL lokal
- **Test** (`application-test.properties`): Testing dengan H2 in-memory database

Jalankan dengan profile test:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### Database Migration
Jika migrasi dari MySQL ke PostgreSQL dengan data existing:

**Cara 1: Menggunakan pgloader (Recommended)**
```bash
brew install pgloader
pgloader mysql://root@localhost/pos_db postgresql://postgres@localhost/pos_db
```

**Cara 2: Manual Export/Import**
```bash
# Export dari MySQL
mysqldump -u root pos_db > pos_db_backup.sql

# Edit file SQL untuk menyesuaikan syntax PostgreSQL
# Kemudian import ke PostgreSQL
psql -U postgres -d pos_db -f pos_db_backup.sql
```

## 🐛 Troubleshooting

### Error: "Access denied" atau 403
- Pastikan sudah login terlebih dahulu
- Cek role user (Admin/Kasir) sesuai dengan endpoint yang dipanggil
- Gunakan curl atau Postman, bukan Swagger UI untuk testing session

### Error: "User is deactivated"
- User dengan `active=false` tidak bisa login
- Admin bisa mengaktifkan kembali dengan update database manual atau endpoint khusus

### Error: Connection refused PostgreSQL
```bash
# Cek status PostgreSQL
brew services list

# Start PostgreSQL
brew services start postgresql@15

# Test koneksi
psql -U postgres -d pos_db
```

### Error: Table not found
- Pastikan `spring.jpa.hibernate.ddl-auto=update` di application.properties
- Restart aplikasi untuk Hibernate membuat tabel otomatis
- Atau buat tabel manual dengan SQL

## 📞 Support & Contributing

- Untuk bug reports, buka issue di repository
- Untuk feature requests, buat pull request
- Documentation: lihat Swagger UI saat aplikasi running

## 📜 License

Copyright © 2025

---

**Tech Stack Summary:**
- Backend: Spring Boot 3.2.5, Java 17
- Database: PostgreSQL
- ORM: Hibernate/JPA
- Security: Spring Security (Session-based)
- Documentation: Swagger/OpenAPI
- Build Tool: Maven
