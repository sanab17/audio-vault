# AudioVault

**Audio Recording Management Platform**

A microservices-based platform for storing, searching, and retrieving audio recordings with secure access controls. Built to demonstrate enterprise-grade Java architecture using Spring Boot, React, Apache Kafka, Docker, and Kubernetes.

---

## 🎯 Project Overview

AudioVault is a full-stack audio management system inspired by production call recording systems. This project showcases:
- Microservices architecture with event-driven communication
- Modern Java/Spring Boot backend development
- Cloud-native deployment with Docker and Kubernetes
- Enterprise design patterns and best practices

**Built to strengthen Java full-stack skills for enterprise roles.**

---

## 🛠 Tech Stack

### Backend
- **Java 21** with Spring Boot 3.2
- **Spring MVC** - REST APIs
- **Spring Data JPA** + **Hibernate** - Database access
- **Spring Security** - JWT authentication _(upcoming)_
- **Apache Kafka** - Event streaming _(upcoming)_
- **PostgreSQL** - Relational data
- **MongoDB** - Search indexing _(upcoming)_
- **MinIO** - S3-compatible object storage _(in progress)_

### Frontend _(upcoming)_
- **React 18** with TypeScript
- **Redux Toolkit** - State management
- **Material-UI** - UI components

### Infrastructure
- **Docker** - Containerization
- **Kubernetes** - Orchestration _(upcoming)_
- **GitHub Actions** - CI/CD _(upcoming)_

---

## 🚀 Current Status

### ✅ Phase 1: Basic Spring Boot Setup (COMPLETE)
- Spring Boot application with health check endpoint
- Maven build configuration
- Development environment setup

### ✅ Phase 2: PostgreSQL + CRUD Operations (COMPLETE)
- Docker Compose with PostgreSQL 16
- Recording entity with JPA/Hibernate
- Repository and service layers
- Full CRUD REST endpoints
- Search functionality by filename

### ✅ Phase 3: File Upload to Local Storage (COMPLETE)
- MultipartFile handling for audio uploads
- File storage service with UUID-based naming
- File download endpoint with proper HTTP headers
- Automatic file cleanup on record deletion
- File type validation (audio/* only)

### 🔄 Phase 4: MinIO S3-Compatible Storage (IN PROGRESS)
- Replace local storage with MinIO object storage
- Pre-signed URL generation for secure downloads
- AWS S3-compatible API

### 📅 Upcoming Phases
- **Phase 5:** Pre-signed URLs
- **Phase 6:** Advanced search with JPA Specifications
- **Phase 7:** Kafka event streaming
- **Phase 8:** Search microservice with MongoDB
- **Phase 9:** React frontend
- **Phase 10:** Redux state management
- **Phase 11:** Kubernetes deployment

---

## 📋 Features (So Far)

- ✅ Upload audio files (.mp3, .wav, .m4a)
- ✅ Store file metadata in PostgreSQL
- ✅ Download recordings with proper content-type headers
- ✅ Search recordings by filename (case-insensitive)
- ✅ Delete recordings (file + database record)
- ✅ Unique filename generation (UUID-based)
- ✅ File size tracking
- ✅ Docker Compose for local development

---

## 🏃 Quick Start

### Prerequisites
- Java 21
- Maven 3.8+
- Docker Desktop

### Run the Application
```bash
# 1. Start PostgreSQL
docker compose up -d

# 2. Run Spring Boot
cd backend
mvn spring-boot:run

# 3. Test health endpoint
curl http://localhost:8081/api/health
```

### API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Health check |
| POST | `/api/recordings/upload` | Upload audio file |
| GET | `/api/recordings` | Get all recordings |
| GET | `/api/recordings/{id}` | Get recording by ID |
| GET | `/api/recordings/{id}/download` | Download audio file |
| GET | `/api/recordings/search?fileName=xyz` | Search by filename |
| DELETE | `/api/recordings/{id}` | Delete recording |

---

## 📁 Project Structure
```
audio-vault/
├── backend/
│   ├── src/main/java/com/audiovault/
│   │   ├── controller/       # REST endpoints
│   │   ├── service/          # Business logic
│   │   ├── repository/       # Data access
│   │   ├── model/            # JPA entities
│   │   └── AudioVaultApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
├── uploads/                  # Uploaded files (Phase 3)
├── docker-compose.yml        # PostgreSQL setup
└── README.md
```

---

## 🎓 Learning Goals

This project demonstrates:
- ✅ **Spring Boot** - Modern Java application development
- ✅ **REST API Design** - Best practices and proper HTTP semantics
- ✅ **JPA/Hibernate** - ORM and database relationships
- ✅ **Docker** - Containerized development environment
- ✅ **File Upload Handling** - MultipartFile processing
- 🔄 **Microservices** - Service decomposition _(in progress)_
- 📅 **Event-Driven Architecture** - Kafka messaging _(upcoming)_
- 📅 **Design Patterns** - Repository, Factory, Observer _(upcoming)_
- 📅 **Frontend Integration** - React with TypeScript _(upcoming)_

---

## 🔗 Connect

**LinkedIn:** [linkedin.com/in/sanabulbule](https://www.linkedin.com/in/sanabulbule/)  
**GitHub:** [github.com/sanab17](https://github.com/sanab17)

---

## 📝 License

MIT License - Personal learning project

---

**Built by Sana Bulbule**