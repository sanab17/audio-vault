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


## What's New in Phase 2 ✅

- ✅ PostgreSQL database running in Docker
- ✅ `Recording` entity with JPA/Hibernate
- ✅ Repository layer (Spring Data JPA)
- ✅ Service layer with business logic
- ✅ REST Controller with full CRUD operations
- ✅ Validation and error handling

---

## Prerequisites

- Java 21
- Maven 3.8+
- **Docker Desktop** (for PostgreSQL)

---

## Step-by-Step Setup

### 1. Start PostgreSQL Database

```bash
# Start Docker Desktop first, then:
docker-compose up -d

# Verify it's running
docker ps

# You should see: audiovault-postgres
```

**What this does:**
- Starts PostgreSQL 16 on port 5432
- Creates database: `audiovault`
- Username: `admin` / Password: `admin123`
- Data persists in Docker volume

### 2. Run the Application

```bash
cd backend
mvn spring-boot:run
```

**Watch for these log messages:**
```
Hibernate: create table recordings (...)
Started AudioVaultApplication in X.XXX seconds
```

This means:
- ✅ Connected to PostgreSQL
- ✅ Created `recordings` table automatically
- ✅ Application is ready

---

## Testing the API

### Test 1: Health Check (Verify It's Running)

```bash
curl http://localhost:8081/api/health
```

**Expected:**
```json
{
  "status": "UP",
  "service": "AudioVault",
  "version": "1.0.0",
  "timestamp": "2024-02-18T..."
}
```

---

### Test 2: Create a Recording (POST)

```bash
curl -X POST http://localhost:8081/api/recordings \
  -H "Content-Type: application/json" \
  -d '{
    "fileName": "meeting-2024-02-18.mp3",
    "duration": 1850
  }'
```

**Expected Response:**
```json
{
  "id": 1,
  "fileName": "meeting-2024-02-18.mp3",
  "duration": 1850,
  "uploadDate": "2024-02-18T10:30:00.123"
}
```

**What happened:**
- ✅ Saved to PostgreSQL `recordings` table
- ✅ Auto-generated ID
- ✅ Auto-set uploadDate timestamp

---

### Test 3: Create More Recordings

```bash
# Recording 2
curl -X POST http://localhost:8081/api/recordings \
  -H "Content-Type: application/json" \
  -d '{
    "fileName": "sales-call-john-doe.wav",
    "duration": 420
  }'

# Recording 3
curl -X POST http://localhost:8081/api/recordings \
  -H "Content-Type: application/json" \
  -d '{
    "fileName": "interview-candidate-123.mp3",
    "duration": 2400
  }'
```

---

### Test 4: Get All Recordings (GET)

```bash
curl http://localhost:8081/api/recordings
```

**Expected:**
```json
[
  {
    "id": 1,
    "fileName": "meeting-2024-02-18.mp3",
    "duration": 1850,
    "uploadDate": "2024-02-18T10:30:00.123"
  },
  {
    "id": 2,
    "fileName": "sales-call-john-doe.wav",
    "duration": 420,
    "uploadDate": "2024-02-18T10:31:00.456"
  },
  {
    "id": 3,
    "fileName": "interview-candidate-123.mp3",
    "duration": 2400,
    "uploadDate": "2024-02-18T10:32:00.789"
  }
]
```

---

### Test 5: Get Recording by ID

```bash
curl http://localhost:8081/api/recordings/1
```

**Expected:**
```json
{
  "id": 1,
  "fileName": "meeting-2024-02-18.mp3",
  "duration": 1850,
  "uploadDate": "2024-02-18T10:30:00.123"
}
```

---

### Test 6: Search by File Name

```bash
curl "http://localhost:8081/api/recordings/search?fileName=sales"
```

**Expected:** Returns only recordings with "sales" in the filename
```json
[
  {
    "id": 2,
    "fileName": "sales-call-john-doe.wav",
    "duration": 420,
    "uploadDate": "2024-02-18T10:31:00.456"
  }
]
```

---

### Test 7: Delete a Recording

```bash
curl -X DELETE http://localhost:8081/api/recordings/2
```

**Expected:** 
- HTTP 204 No Content (success)
- Recording deleted from database

**Verify deletion:**
```bash
curl http://localhost:8081/api/recordings
# Recording with ID 2 should be gone
```

---

## API Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/health` | Health check |
| POST | `/api/recordings` | Create new recording |
| GET | `/api/recordings` | Get all recordings |
| GET | `/api/recordings/{id}` | Get recording by ID |
| GET | `/api/recordings/search?fileName=xyz` | Search by filename |
| DELETE | `/api/recordings/{id}` | Delete recording |

---

## Database Structure

**Table:** `recordings`

| Column | Type | Description |
|--------|------|-------------|
| id | BIGINT | Primary key (auto-increment) |
| file_name | VARCHAR(255) | Recording filename (required) |
| duration | INTEGER | Duration in seconds (required) |
| upload_date | TIMESTAMP | Auto-set on creation |

---

## Architecture (So Far)

```
┌─────────────────┐
│   REST Client   │  (curl, Postman, Frontend)
│  (You/Browser)  │
└────────┬────────┘
         │ HTTP
         ↓
┌─────────────────────────────────────────┐
│         RecordingController             │  @RestController
│  - POST   /api/recordings               │
│  - GET    /api/recordings               │
│  - GET    /api/recordings/{id}          │
│  - DELETE /api/recordings/{id}          │
└────────┬────────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────────┐
│         RecordingService                │  @Service
│  - Business logic                       │
│  - Transactions                         │
└────────┬────────────────────────────────┘
         │
         ↓
┌─────────────────────────────────────────┐
│      RecordingRepository                │  @Repository
│  extends JpaRepository                  │
│  - Spring Data JPA magic                │
└────────┬────────────────────────────────┘
         │ JPA/Hibernate
         ↓
┌─────────────────────────────────────────┐
│         PostgreSQL Database             │
│  Table: recordings                      │
│  Port: 5432                             │
└─────────────────────────────────────────┘
```

---

## What You Just Learned

✅ **JPA Entities** - How to map Java classes to database tables  
✅ **Spring Data JPA** - Repository pattern (no SQL needed!)  
✅ **Service Layer** - Separation of concerns  
✅ **REST Controllers** - HTTP endpoints with proper status codes  
✅ **Docker Compose** - Running databases in containers  
✅ **Hibernate** - Auto-creates tables from entities (`ddl-auto=update`)  
✅ **Validation** - `@NotBlank`, `@Positive` annotations  
✅ **Logging** - Using SLF4J with Lombok's `@Slf4j`  

---

## Troubleshooting

### Port 5432 already in use?
```bash
# Stop other PostgreSQL instances
brew services stop postgresql

# Or change port in docker-compose.yml and application.properties
```

### Can't connect to database?
```bash
# Check if PostgreSQL container is running
docker ps

# Check container logs
docker logs audiovault-postgres

# Restart containers
docker-compose down
docker-compose up -d
```

### Application won't start?
```bash
# Clean Maven build
mvn clean install

# Check for typos in application.properties
```

### See all SQL queries?
Already enabled! Check your terminal logs for:
```
Hibernate: insert into recordings (duration, file_name, upload_date, id) values (?, ?, ?, default)
```

---

## View Data Directly in PostgreSQL

**Using Docker:**
```bash
docker exec -it audiovault-postgres psql -U admin -d audiovault

# Inside PostgreSQL:
\dt                           # List tables
SELECT * FROM recordings;     # View data
\q                            # Quit
```

---

## Next Steps (Phase 3)

- Add actual file upload functionality
- Store uploaded files in local `/uploads` folder
- Return file path in the response

---

### Phase 3: File Upload to Local Storage

## What's New in Phase 3 ✅

- ✅ Actual file upload functionality (MultipartFile)
- ✅ Files stored in local `/uploads` folder
- ✅ Updated `Recording` entity with `filePath`, `fileSize`, `contentType`
- ✅ File download endpoint
- ✅ File deletion when recording is deleted
- ✅ File validation (audio files only)

---

## Prerequisites

- Java 21
- Maven 3.8+
- Docker Desktop (PostgreSQL)
- **An audio file for testing** (`.mp3`, `.wav`, etc.)

---

## Step-by-Step Setup

### 1. Start PostgreSQL

```bash
docker-compose up -d
docker ps  # Verify it's running
```

### 2. Run the Application

```bash
cd backend
mvn spring-boot:run
```

**Watch for:**
```
Hibernate: alter table recordings add column content_type varchar(255) not null
Hibernate: alter table recordings add column file_path varchar(255) not null
Hibernate: alter table recordings add column file_size bigint not null
```

✅ **Hibernate automatically updated your table with new columns!**

---

## Testing the API

### Test 1: Upload an Audio File

First, create or download a test audio file. Any `.mp3` or `.wav` file will work.

**Upload with curl:**
```bash
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@/path/to/your/test-audio.mp3" \
  -F "duration=185"
```

**Example (if you have a file on Desktop):**
```bash
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@~/Desktop/test-audio.mp3" \
  -F "duration=185"
```

**Expected Response:**
```json
{
  "id": 1,
  "fileName": "test-audio.mp3",
  "filePath": "uploads/a1b2c3d4-e5f6-7890-abcd-1234567890ab.mp3",
  "duration": 185,
  "fileSize": 4567890,
  "contentType": "audio/mpeg",
  "uploadDate": "2024-02-18T15:30:00.123"
}
```

✅ **Check the `uploads/` folder** - your file is there!

```bash
ls -lh uploads/
# You should see: a1b2c3d4-e5f6-7890-abcd-1234567890ab.mp3
```

---

### Test 2: Upload Multiple Files

```bash
# Upload another file
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@~/Desktop/another-audio.wav" \
  -F "duration=420"
```

---

### Test 3: Get All Recordings (See File Info)

```bash
curl http://localhost:8081/api/recordings
```

**Expected:**
```json
[
  {
    "id": 1,
    "fileName": "test-audio.mp3",
    "filePath": "uploads/a1b2c3d4...mp3",
    "duration": 185,
    "fileSize": 4567890,
    "contentType": "audio/mpeg",
    "uploadDate": "2024-02-18T15:30:00"
  },
  {
    "id": 2,
    "fileName": "another-audio.wav",
    "filePath": "uploads/b2c3d4e5...wav",
    "duration": 420,
    "fileSize": 7890123,
    "contentType": "audio/wav",
    "uploadDate": "2024-02-18T15:32:00"
  }
]
```

---

### Test 4: Download a Recording

```bash
curl http://localhost:8081/api/recordings/1/download --output downloaded.mp3

# Play it to verify!
# macOS:
open downloaded.mp3

# Linux:
xdg-open downloaded.mp3
```

✅ **The file plays!** You just uploaded and downloaded it successfully.

---

### Test 5: Delete a Recording (File Gets Deleted Too)

```bash
curl -X DELETE http://localhost:8081/api/recordings/1

# Check uploads folder - file is gone!
ls uploads/
```

---

## API Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/recordings/upload` | **NEW:** Upload audio file + metadata |
| GET | `/api/recordings/{id}/download` | **NEW:** Download audio file |
| GET | `/api/recordings` | Get all recordings |
| GET | `/api/recordings/{id}` | Get recording metadata by ID |
| DELETE | `/api/recordings/{id}` | Delete recording + file |
| POST | `/api/recordings` | Create metadata only (backward compat.) |

---

## How File Upload Works

### 1. MultipartFile Handling

```java
@PostMapping("/upload")
public ResponseEntity<Recording> uploadRecording(
    @RequestParam("file") MultipartFile file,
    @RequestParam("duration") Integer duration) {
    
    // Spring automatically parses the multipart form data
}
```

**Key Points:**
- `@RequestParam("file")` - Maps to form field named "file"
- `MultipartFile` - Spring's abstraction for uploaded files
- Provides: `getOriginalFilename()`, `getSize()`, `getContentType()`, `getInputStream()`

---

### 2. Unique Filename Generation

```java
String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
// e.g., "a1b2c3d4-e5f6-7890-abcd-1234567890ab.mp3"
```

**Why?**
- Avoids filename collisions (two users upload "meeting.mp3")
- Prevents directory traversal attacks
- Makes files easier to manage

---

### 3. File Storage

```java
Path targetLocation = Paths.get(uploadDir).resolve(uniqueFilename);
Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
```

**What happens:**
1. Creates `uploads/` directory if it doesn't exist
2. Generates unique filename
3. Copies file from HTTP request to disk
4. Returns the file path

---

### 4. Database Record

```java
Recording recording = new Recording();
recording.setFileName(file.getOriginalFilename());  // Original: "test-audio.mp3"
recording.setFilePath(filePath);                    // Stored as: "uploads/uuid.mp3"
recording.setFileSize(file.getSize());              // In bytes
recording.setContentType(file.getContentType());    // "audio/mpeg"
```

---

## Testing with Different File Types

### Valid Audio Files:
```bash
# MP3
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@test.mp3" -F "duration=180"

# WAV
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@test.wav" -F "duration=240"

# M4A
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@test.m4a" -F "duration=300"
```

### Invalid File (Should Fail):
```bash
# Try uploading a text file
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@test.txt" -F "duration=100"

# Expected: HTTP 400 Bad Request
```

---

## Project Structure (Updated)

```
audiovault/
├── docker-compose.yml
├── uploads/                                    # NEW: Uploaded files stored here
│   ├── uuid1.mp3
│   ├── uuid2.wav
│   └── uuid3.m4a
└── backend/
    ├── pom.xml
    └── src/main/
        ├── java/com/audiovault/
        │   ├── AudioVaultApplication.java
        │   ├── controller/
        │   │   ├── HealthController.java
        │   │   └── RecordingController.java       # UPDATED: Upload + Download
        │   ├── model/
        │   │   └── Recording.java                 # UPDATED: Added filePath, fileSize, contentType
        │   ├── repository/
        │   │   └── RecordingRepository.java
        │   └── service/
        │       ├── RecordingService.java          # UPDATED: Handle file uploads
        │       └── FileStorageService.java        # NEW: File I/O operations
        └── resources/
            └── application.properties             # UPDATED: File upload config
```

---

## What You Just Learned

✅ **MultipartFile Handling** - Spring's file upload abstraction  
✅ **File I/O in Java** - `java.nio.file` API  
✅ **UUID Generation** - Unique identifiers for files  
✅ **Content Type Validation** - Restricting file types  
✅ **File Download** - Streaming files to clients with proper headers  
✅ **Resource Cleanup** - Deleting files when records are deleted  
✅ **Spring MVC File Upload** - `@RequestParam("file") MultipartFile`  

---

## Architecture (Phase 3)

```
┌───────────────────────────────────────────┐
│  POST /api/recordings/upload              │
│  - file: MultipartFile                    │
│  - duration: Integer                      │
└────────────┬──────────────────────────────┘
             │
             ↓
┌───────────────────────────────────────────┐
│  RecordingController                      │
│  - Validates file type (audio/* only)     │
└────────────┬──────────────────────────────┘
             │
             ↓
┌───────────────────────────────────────────┐
│  RecordingService                         │
│  - Creates Recording entity               │
│  - Calls FileStorageService               │
└────────────┬──────────────────────────────┘
             │
             ↓
┌───────────────────────────────────────────┐
│  FileStorageService                       │
│  - Generates unique filename (UUID)       │
│  - Saves file to uploads/ directory       │
│  - Returns file path                      │
└────────────┬──────────────────────────────┘
             │
             ↓
┌───────────────────────────────────────────┐
│  Disk: uploads/uuid.mp3                   │
│  Database: recordings table               │
│  - file_path: "uploads/uuid.mp3"          │
└───────────────────────────────────────────┘
```

---

## Troubleshooting

### File upload fails with "Maximum upload size exceeded"

**Problem:** File is larger than 50MB

**Solution 1:** Increase limit in `application.properties`:
```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

**Solution 2:** Use a smaller test file

---

### "Could not create upload directory"

**Problem:** Permission denied creating `uploads/` folder

**Solution:**
```bash
# Create directory manually
mkdir uploads
chmod 755 uploads

# Or run as admin
sudo mvn spring-boot:run
```

---

### File downloaded but won't play

**Problem:** File might be corrupted

**Check:**
```bash
# Compare file sizes
ls -lh uploads/uuid.mp3        # Original
ls -lh downloaded.mp3          # Downloaded

# They should be identical
```

---

### Database error: "column file_path does not exist"

**Problem:** Old database doesn't have new columns

**Solution:**
```bash
# Drop the database and recreate (dev only!)
docker exec -it audiovault-postgres psql -U admin -d audiovault

# Inside PostgreSQL:
DROP TABLE recordings;
\q

# Restart Spring Boot - Hibernate will recreate the table
mvn spring-boot:run
```

---

## Testing Without curl (Using Postman)

1. **Open Postman**
2. **Create POST request** to `http://localhost:8081/api/recordings/upload`
3. **Select "Body" tab**
4. **Choose "form-data"**
5. **Add two fields:**
   - Key: `file` | Type: File | Value: [Select your audio file]
   - Key: `duration` | Type: Text | Value: `185`
6. **Click "Send"**

---

## Next Steps (Phase 4)

In Phase 4, we'll replace local file storage with **MinIO** (S3-compatible object storage):
- Run MinIO in Docker
- Store files in S3-like buckets
- Generate pre-signed URLs for secure downloads
- Prepare for production deployment

---

### Future Phases
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
