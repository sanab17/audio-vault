# AudioVault - Audio Recording Management Platform

A microservices-based audio recording management system built with Spring Boot, React, Apache Kafka, Docker, and Kubernetes.

## Phase 1: Basic Spring Boot Setup ✅

### Technologies
- Java 21
- Spring Boot 3.2.2
- Maven
- Spring Web (REST APIs)

---


## Installation

### 1. Install Java 21 (if not already installed)

**Using SDKMAN (recommended):**
```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.5-tem

# Verify
java -version
```

**Using Homebrew (macOS):**
```bash
brew install openjdk@21
```

### 2. Install Maven

```bash
# Using SDKMAN
sdk install maven

# OR using Homebrew
brew install maven

# Verify
mvn -version
```

---


## Testing the API

### Health Check Endpoint

**Using curl:**
```bash
curl http://localhost:8081/api/health
```

**Using browser:**
```
http://localhost:8081/api/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "service": "AudioVault",
  "version": "1.0.0",
  "timestamp": "2024-02-18T10:30:00.123"
}
```


---

## Project Structure
```
audiovault/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/audiovault/
│   │   │   │   ├── AudioVaultApplication.java      # Main application
│   │   │   │   └── controller/
│   │   │   │       └── HealthController.java       # Health check endpoint
│   │   │   └── resources/
│   │   │       └── application.properties          # Configuration
│   │   └── test/
│   ├── pom.xml                                     # Maven dependencies
│   └── README.md
└── frontend/                                       # (Coming in Phase 9)
```

---

## VSCode Extensions (Recommended)

Install these for the best development experience:
- **Extension Pack for Java** (Microsoft)
- **Spring Boot Extension Pack** (VMware)
- **Lombok Annotations Support**

---

## Troubleshooting

### Port 8081 already in use?
```bash
# Option 1: Kill the process using port 8081
lsof -ti:8081 | xargs kill -9

# Option 2: Change port in application.properties
server.port=8082
```

### Maven build issues?
```bash
# Clean and rebuild
mvn clean install
```

### Java version mismatch?
```bash
# Check Java version
java -version

# Should show Java 21
```

---

## What's Next?

### Phase 2: Add PostgreSQL + First Entity
- Docker Compose setup for PostgreSQL
- Create `Recording` entity
- Implement basic CRUD operations
- Test with Postman

### Future Phases
- **Phase 3:** File upload to local storage
- **Phase 4:** MinIO (S3-compatible) integration
- **Phase 5:** Pre-signed URLs
- **Phase 6:** Search and filtering
- **Phase 7:** Kafka event streaming
- **Phase 8:** Search microservice (MongoDB)
- **Phase 9:** React frontend
- **Phase 10:** Redux state management
- **Phase 11:** Docker + Kubernetes deployment

---

## Tech Stack (Full System)

**Backend:**
- Java 17, Spring Boot, Spring MVC, Spring Security
- Spring Data JPA, Hibernate
- Apache Kafka
- PostgreSQL, MongoDB

**Frontend:**
- React, TypeScript
- Redux Toolkit
- Axios

**Infrastructure:**
- Docker, Kubernetes
- MinIO (S3-compatible storage)
- GitHub Actions (CI/CD)

---

## GitHub Repository

**Coming soon:** `https://github.com/sanab17/audiovault`

---

## Contact

Built by **Sana Bulbule**  
[LinkedIn](https://www.linkedin.com/in/sanabulbule/) | [GitHub](https://github.com/sanab17)
