# 🚀 Spring Boot Microservices Ecosystem

A robust, scalable microservices architecture built with **Spring Boot 3**, **Spring Cloud Gateway**, **Netflix Eureka**, and **OpenFeign**, featuring centralized exception handling and Resilience4j circuit breaker fallback mechanics.

---

## 🛠️ Tech Stack & Dependencies

| Tool / Framework | Purpose |
| :--- | :--- |
| **Java 21** | Core Programming Language |
| **Spring Boot 3.2.5** | Microservice Framework |
| **Spring Cloud Gateway** | API Gateway & Reactive Routing (WebFlux) |
| **Netflix Eureka** | Service Registry & Discovery |
| **OpenFeign** | Declarative REST Client (Inter-service Communication) |
| **Resilience4j** | Circuit Breaker & Fallback Mechanisms |
| **Jackson / Lombok** | JSON Serialization & Boilerplate reduction |

---

## 🏗️ System Architecture


+-------------------+
                  |   Client Request  |
                  +---------+---------+
                            |
                            v
                 +---------------------+
                 |  API Gateway (9090) |
                 +----------+----------+
                            |
        +-------------------+-------------------+
        |                                       |
        v                                       v
+------------------+                    +------------------+
| Employee Service | <--- OpenFeign --- |  Address Service |
+--------+---------+                    +--------+---------+
|                                       |
+-------------------+-------------------+
|
v
+--------------------+
|   Eureka Server    |
+--------------------+


---

## 🧩 Microservices Breakdown

### 1. 🌐 API Gateway (`Port: 9090`)
* Acts as the single entry point for all client requests.
* Implements dynamic route mapping and reactive non-blocking execution using Spring WebFlux.
* Configured with **Fallback Endpoints** for graceful service failure degrade.

### 2. 🔍 Service Registry (Eureka Server)
* Maintains dynamic registry of active microservice instances.
* Enables load balancing and automatic service discovery without hardcoded IPs.

### 3. 🏢 Address Service
* Handles address management and invokes `Employee Service` via **OpenFeign**.
* Integrates `CustomErrorDecoder` and `GlobalExceptionHandler` to translate downstream failures into clean status responses.

### 4. 👨‍💻 Employee Service
* Manages core employee records and exposes internal endpoints consumed by other microservices.

---

## ⚡ Key Features & Resilience Strategy

* **Inter-Service Error Decoding (`CustomErrorDecoder`):** Automatically intercepts Feign client errors (500/503/service down) and formats them into custom domain exceptions instead of raw stack traces.
* **Global Exception Management:** Centralized `@RestControllerAdvice` ensures standardized JSON responses across all HTTP errors.
* **Circuit Breaker Fallbacks:** Integrated Resilience4j fallback mappings at Gateway and Service levels.

---

## 📌 Services & Port Mapping

| Service Name | Port | Description |
| :--- | :--- | :--- |
| **Eureka Server** | `8761` | Discovery Registry |
| **API Gateway** | `9090` | Routing & Central Gateway |
| **Address Service** | Dynamic / Configured | Address Domain API |
| **Employee Service**| Dynamic / Configured | Employee Domain API |

---

## 🚦 Getting Started

### Prerequisites
* **Java 21** installed
* **Maven 3.8+** installed

### Execution Order
Services **must** be launched in the following sequence to allow proper registration:

1. **Start Eureka Server**
   ```bash
   cd eureka-server
   mvn spring-boot:run
