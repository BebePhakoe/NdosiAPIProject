Project Overview: Beloveds Test Automation Framework
This repository contains a professional-grade API automation framework built to validate the Ndosi Simplified Automation platform. The project focuses on end-to-end validation of user lifecycles, role-based access control, and full-stack data integrity from the API to the database.

🚀 Key Features
Hybrid Framework: Integrates RestAssured for API testing with JDBC for real-time MariaDB/MySQL database verification.

Modular Architecture: Implements a decoupled design separating test logic, dynamic payload construction, and utility engines.

Dynamic Data Handling: Uses Java Faker to generate unique user data, ensuring tests are repeatable and independent.

Automated Database Resets: Includes custom SQL maintenance scripts to ensure a "clean slate" for every test execution.

🛠️ Tech Stack
Language: Java 11+

Testing Engine: TestNG

API Client: RestAssured

Database: MariaDB / MySQL (Managed via JDBC)

Build Tool: Maven

IDE: IntelliJ IDEA

📂 Framework Structure
common/: Global configurations and environment constants (e.g., Baseurl).

utils/: The "Engine Room" containing DatabaseConnection and API Requests.

PayloadBuilder/: A dedicated layer for constructing complex JSON strings for API endpoints.

tests/: The "Command Center" containing the actual test scenarios (e.g., UserRegistration).
