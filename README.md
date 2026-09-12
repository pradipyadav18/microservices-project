# 🚀 Enterprise Spring Boot Microservices Ecosystem

A robust, production-ready Microservices Architecture built using **Spring Boot 3**, **Spring Cloud**, **Resilience4j**, and **JWT Authentication**. This project demonstrates end-to-end distributed system capabilities, including service registry, reactive API routing, automated circuit breaking, and secure inter-service communication.

---

## 🛠️ Tech Stack & Dependencies

* **Language & Framework**: Java 17/21, Spring Boot 3.2.5
* **Cloud Infrastructure**: Spring Cloud Gateway 4.1.2, Netflix Eureka Server
* **Security**: JWT (JSON Web Tokens), Spring Security
* **Fault Tolerance**: Resilience4j (Circuit Breaker & TimeLimiter)
* **Inter-Service Communication**: OpenFeign, WebClient
* **Database & Persistence**: MySQL, Spring Data JPA, Hibernate
* **Tools & Build**: Maven, Postman, Git

---

## 🏗️ System Architecture


+-----------------------+
                              |    Service Registry   |
                              |    (Eureka Server)    |
                              +-----------+-----------+
                                          ^
                                          | (Registration)
                                          v
+------------------+             +------------+------------+             +--------------------+
|   Client / UI    | ----------> |    Spring Cloud Gateway    | ----------> |    Auth Service    |
+------------------+             |    (Port: 9090 / JWT)    |             |    (Port: 8088)    |
+------------+------------+             +--------------------+
|
| (Routed Requests)
v
+------------+------------+
|    Employee Service     |
|      (Port: 8080)       |
+------------+------------+
|
| (Feign Client / REST)
v
+------------+------------+
|     Address Service     |
|      (Port: 8081)       |
+-------------------------+




---

## 📦 Services Breakdown

| Service Name | Port | Description |
| :--- | :--- | :--- |
| **Service Registry** | `8761` | Eureka Server for dynamic service discovery and heartbeat monitoring. |
| **Auth Service** | `8088` | Handles user authentication, password hashing, and JWT token issuance. |
| **API Gateway** | `9090` | Unified entry point with JWT validation, dynamic routing, and Resilience4j logic. |
| **Employee Service** | `8080` | Manages core employee domain data and communicates with Address Service. |
| **Address Service** | `8081` | Manages location details linked to employees (PERMANENT, TEMPORARY). |

---

## ✨ Key Technical Highlights

* **Centralized API Security**: Requests pass through the API Gateway, where JWT tokens are validated before reaching downstream services.
* **Resilience4j Circuit Breaker & Timeout**:
  * Configured via `ReactiveResilience4JCircuitBreakerFactory`.
  * Enforces a strict **5-second timeout** limit per downstream call.
  * Gracefully redirects long-running or failed requests to a fallback endpoint (`503 Service Unavailable`).
* **Declarative REST Client**: Uses **OpenFeign** for seamless synchronous calls between `Employee-Service` and `Address-Service`.
* **Database & Entity Mapping**: One-to-Many mapping for aggregated DTO responses (Employee details with nested Address list).

---

## ⚡ Setup & Execution Guide

### Prerequisites
1. Installed **JDK 17** or higher.
2. **Maven 3.8+** installed.
3. Database (MySQL/PostgreSQL) active on local instance.

### Running the Ecosystem
Start the microservices in the exact order below to ensure correct registration:

```bash
# 1. Start Eureka Registry
cd service-registry && mvn spring-boot:run

# 2. Start Auth Service
cd auth-service && mvn spring-boot:run

# 3. Start Address Service
cd address-service && mvn spring-boot:run

# 4. Start Employee Service
cd employee-service && mvn spring-boot:run

# 5. Start API Gateway
cd api-gateway && mvn spring-boot:run
