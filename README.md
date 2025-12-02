# API Backend - POS System

Dafa Adrenalin Pratama XI RPL A

Backend API untuk sistem Point of Sale (POS) dengan role-based access control.

## 📋 Deskripsi

Sistem backend API untuk POS dengan 2 role:
- **Kasir**: Dapat melakukan transaksi, melihat produk dan kategori
- **Admin**: Memiliki akses penuh untuk CRUD produk, kategori, dan user management

## 🚀 Teknologi

- Java 17
- Spring Boot 3.4.0
- MySQL
- Maven
- Swagger/OpenAPI untuk dokumentasi API
- Spring Security untuk authentication
- Lombok untuk reduce boilerplate code

## 📦 Struktur Database

### Tables:
1. **users** - Menyimpan data user (Kasir & Admin)
2. **categories** - Kategori produk
3. **products** - Data produk
4. **transactions** - Header transaksi
5. **transaction_items** - Detail item transaksi

## 🔧 Instalasi

### Prerequisites
- Java 25
- MySQL 8.0+
- Maven 3.6+

### Setup Database

1. Buat database MySQL:
```sql
CREATE DATABASE pos_db;
```

2. Update konfigurasi database di `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pos_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Run Application

```bash
mvn clean install
mvn spring-boot:run
```

Aplikasi akan berjalan di `http://localhost:8080`

## 📚 API Documentation

Setelah aplikasi berjalan, akses Swagger UI di:
```
http://localhost:8080/swagger-ui.html
```

## 🔐 Authentication

Sistem menggunakan **Session-based authentication**. 

### Flow Authentication:
1. Login dengan endpoint `/api/auth/login`
2. Session akan disimpan dan cookie JSESSIONID akan dikirim
3. Gunakan cookie tersebut untuk request selanjutnya
4. Logout dengan endpoint `/api/auth/logout`

## 📝 API Endpoints

### Authentication (Semua Akses)
- `POST /api/auth/register` - Register user baru
- `POST /api/auth/login` - Login
- `POST /api/auth/logout` - Logout
- `GET /api/auth/profile` - Get profile

### Categories
- `GET /api/categories` - Get semua kategori (All)
- `POST /api/categories` - Create kategori (Admin only)
- `PUT /api/categories/{id}` - Update kategori (Admin only)
- `DELETE /api/categories/{id}` - Delete kategori (Admin only)

### Products
- `GET /api/products` - Get semua produk (All)
- `GET /api/products/{id}` - Get produk by ID (All)
- `POST /api/products` - Create produk (Admin only)
- `PUT /api/products/{id}` - Update produk (Admin only)
- `DELETE /api/products/{id}` - Delete produk (Admin only)

### Transactions (Kasir & Admin)
- `GET /api/transactions` - Get semua transaksi
- `GET /api/transactions/{id}` - Get transaksi by ID
- `POST /api/transactions` - Create transaksi baru
- `PUT /api/transactions/{id}` - Update transaksi (tambah 1 item)

### Users (Admin only)
- `PUT /api/users/{id}/deactivate` - Deactivate user

## 🧪 Testing

```bash
mvn test
```

## 📄 Contoh Request

### Register
```json
POST /api/auth/register
{
  "username": "kasir1",
  "password": "password123",
  "name": "Kasir Satu",
  "role": "KASIR"
}
```

### Login
```json
POST /api/auth/login
{
  "username": "kasir1",
  "password": "password123"
}
```

### Create Product (Admin)
```json
POST /api/products
{
  "name": "Kopi Susu",
  "description": "Kopi susu segar",
  "price": 15000,
  "stock": 100,
  "categoryId": 1
}
```

### Create Transaction
```json
POST /api/transactions
{
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

## 🔒 Security

- Password di-encrypt menggunakan BCrypt
- Session timeout: 30 menit
- CSRF protection disabled (untuk testing, enable di production)

## 📈 Development

### Build untuk Production
```bash
mvn clean package
java -jar target/jp-api-backend-1.0.0.jar
```

## 📞 Support

Untuk pertanyaan atau issue, silakan hubungi support@example.com

## 📜 License

Copyright © 2025
