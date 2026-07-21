# ⌨️ TypeMaster - Spring Boot & MySQL Typing Practice Application

A clean, modern, responsive typing practice web application powered by **Java 17**, **Spring Boot 3**, **Spring Data JPA**, **MySQL Database**, **HTML5**, **CSS3 (Poppins)**, and **Vanilla JavaScript (ES6)**.

---

## 📁 Directory & File Structure

```text
TypeMaster/
├── pom.xml                                           // Maven build configuration (Spring Boot, MySQL Driver, JPA)
├── README.md                                         // Project documentation & setup instructions
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── typemaster/
    │   │           ├── TypeMasterApplication.java    // Spring Boot Main Entry Point
    │   │           ├── config/
    │   │           │   └── DataInitializer.java      // Seeds MySQL database with practice paragraphs on startup
    │   │           ├── controller/
    │   │           │   ├── PageController.java       // View navigation routes (/, /typing, /result)
    │   │           │   ├── ParagraphController.java  // REST Controller for paragraphs (/api/paragraphs)
    │   │           │   └── TestResultController.java // REST Controller for test results (/api/results)
    │   │           ├── dto/
    │   │           │   ├── ParagraphDTO.java
    │   │           │   └── TestResultDTO.java
    │   │           ├── model/
    │   │           │   ├── Paragraph.java            // JPA Entity mapped to 'paragraphs' table
    │   │           │   └── TestResult.java           // JPA Entity mapped to 'test_results' table
    │   │           ├── repository/
    │   │           │   ├── ParagraphRepository.java
    │   │           │   └── TestResultRepository.java
    │   │           └── service/
    │   │               ├── ParagraphService.java
    │   │               └── TestResultService.java
    │   │
    │   └── resources/
    │       ├── application.properties               // MySQL Database connection settings (Database: typemaster)
    │       └── static/                              // Static web assets (Poppins typography)
    │           ├── index.html
    │           ├── typing.html
    │           ├── result.html
    │           └── assets/
    │               ├── css/ (style.css, home.css, typing.css, result.css)
    │               └── js/ (app.js, data.js, stats.js, timer.js, typing.js)
```

---

## 🗄️ MySQL Database Setup & Configuration

The application is configured to connect to MySQL and automatically create the database **`typemaster`** if it does not already exist.

### `src/main/resources/application.properties`

```properties
server.port=8084

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/typemaster?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=root

# Hibernate DDL Auto & Dialect
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

> 💡 **Note**: Update `spring.datasource.username` and `spring.datasource.password` in `application.properties` to match your local MySQL credentials if different from `root`/`root`.

---

## 🚀 How to Run

1. Ensure MySQL Server is running locally on port `3306`.
2. Open terminal in `TypeMaster` directory and run:
   ```bash
   mvn spring-boot:run
   ```
3. Open your web browser and visit:
   `http://localhost:8084`
