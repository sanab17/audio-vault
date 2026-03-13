# AudioVault - Phase 8: Search Microservice with MongoDB

## What's New in Phase 8 ✅

- ✅ **Second microservice** - Search Service (separate Spring Boot app)
- ✅ **MongoDB** - Document database for optimized search
- ✅ **Kafka Consumer** - Listens to events from Recording Service
- ✅ **Event-driven data sync** - Eventual consistency pattern
- ✅ **Dedicated search API** - Advanced queries and filters
- ✅ **Polyglot persistence** - PostgreSQL + MongoDB
- ✅ **True microservices** - Independent services communicating via events
- ✅ **Full-text search** - MongoDB text indexes

---

## Microservices Architecture

```
┌──────────────────────────────────────────┐
│  Recording Service (Port 8081)           │
│  - Upload audio files                    │
│  - Store in PostgreSQL                   │
│  - Store files in MinIO                  │
│  - Publish Kafka events                  │
└────────────┬─────────────────────────────┘
             │
             ↓ Kafka Topics:
             │ - recording.created
             │ - recording.updated  
             │ - recording.deleted
             ↓
┌──────────────────────────────────────────┐
│  Search Service (Port 8082)              │  ← NEW!
│  - Consume Kafka events                  │
│  - Index data in MongoDB                 │
│  - Provide search API                    │
│  - Full-text search                      │
│  - Aggregation queries                   │
└──────────────────────────────────────────┘
```

---

## Why Two Services?

### Recording Service (8081)
**Purpose:** Transactional operations  
**Database:** PostgreSQL (ACID compliance)  
**Responsibilities:**
- Upload files
- Store metadata
- Manage file lifecycle
- Publish events

### Search Service (8082)
**Purpose:** Optimized queries  
**Database:** MongoDB (flexible, fast search)  
**Responsibilities:**
- Full-text search
- Range queries
- Aggregations
- Statistics

**Benefits:**
- ✅ Each service can scale independently
- ✅ Search doesn't slow down uploads
- ✅ Use best database for each job
- ✅ Services can be deployed separately

---

## Prerequisites

- Java 21
- Maven 3.8+
- Docker Desktop

---

## Step-by-Step Setup

### 1. Start All Infrastructure

```bash
docker compose up -d

# Verify all 5 containers are running:
docker ps

# Should see:
# - audiovault-postgres (5432)
# - audiovault-minio (9000, 9001)
# - audiovault-zookeeper (2181)
# - audiovault-kafka (9092)
# - audiovault-mongodb (27017)  ← NEW!
```

---

### 2. Run Recording Service (Terminal 1)

```bash
# In your existing recording-service directory
cd recording-service
mvn spring-boot:run
```

**Watch for:**
```
Started AudioVaultApplication in X.XXX seconds (JVM running for Y.YYY)
```

**Service running on:** http://localhost:8081

---

### 3. Run Search Service (Terminal 2)

```bash
# In new terminal
cd search-service
mvn spring-boot:run
```

**Watch for:**
```
Started SearchServiceApplication in X.XXX seconds
```

**Service running on:** http://localhost:8082

---

## Testing the Microservices

### Test 1: Verify Both Services Are Running

**Recording Service:**
```bash
curl http://localhost:8081/api/health
```

**Expected:**
```json
{"status":"UP","service":"AudioVault","version":"1.0.0",...}
```

**Search Service:**
```bash
curl http://localhost:8082/api/search/health
```

**Expected:**
```json
{"status":"UP","service":"Search Service","version":"1.0.0",...}
```

✅ **Both services responding!**

---

### Test 2: Upload File (Recording Service Publishes Event)

```bash
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@$HOME/Desktop/SoundHelix-Song-1.mp3" \
  -F "duration=185"
```

**What happens:**
1. ✅ Recording Service saves to PostgreSQL
2. ✅ File stored in MinIO
3. ✅ Kafka event published to `recording.created` topic
4. ✅ Search Service consumes event
5. ✅ Recording indexed in MongoDB

**Check Search Service logs:**
```
Received CREATED event for recording ID: 1, fileName: SoundHelix-Song-1.mp3
Successfully indexed recording ID: 1
```

---

### Test 3: Search for the Recording (Search Service)

```bash
curl "http://localhost:8082/api/search/recordings/by-name?fileName=SoundHelix"
```

**Expected:**
```json
[
  {
    "id": "65abc123...",
    "recordingId": 1,
    "fileName": "SoundHelix-Song-1.mp3",
    "filePath": "uuid.mp3",
    "duration": 185,
    "fileSize": 4567890,
    "contentType": "audio/mpeg",
    "uploadDate": "2026-02-26T12:00:00",
    "lastModified": "2026-02-26T12:00:00",
    "fileNameLower": "soundhelix-song-1.mp3",
    "fileExtension": "mp3"
  }
]
```

✅ **Data synchronized from PostgreSQL to MongoDB via Kafka!**

---

### Test 4: Upload Multiple Files

```bash
# Upload 3 more files
curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@$HOME/Desktop/meeting-2024.mp3" \
  -F "duration=420"

curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@$HOME/Desktop/interview.wav" \
  -F "duration=600"

curl -X POST http://localhost:8081/api/recordings/upload \
  -F "file=@$HOME/Desktop/podcast-episode.mp3" \
  -F "duration=3600"
```

**Check Search Service has all 4:**
```bash
curl http://localhost:8082/api/search/recordings
```

---

### Test 5: Advanced Search (Only Available in Search Service)

**Search by extension:**
```bash
curl "http://localhost:8082/api/search/recordings/by-extension?extension=mp3"
```

**Search by duration range (1-10 minutes):**
```bash
curl "http://localhost:8082/api/search/recordings/by-duration?min=60&max=600"
```

**Search by file size (1MB - 10MB):**
```bash
curl "http://localhost:8082/api/search/recordings/by-size?min=1000000&max=10000000"
```

**Complex search (file name + duration):**
```bash
curl "http://localhost:8082/api/search/recordings/complex?fileName=meeting&minDuration=300&maxDuration=900"
```

✅ **These queries are FAST in MongoDB!**

---

### Test 6: Get Statistics

```bash
curl http://localhost:8082/api/search/stats
```

**Expected:**
```json
{
  "totalRecordings": 4,
  "totalFileSizeBytes": 18271560,
  "totalFileSizeMB": 17.42,
  "averageDurationSeconds": 951.25,
  "timestamp": "2026-02-26T12:30:00"
}
```

---

### Test 7: Delete Recording (Event Synchronization)

```bash
# Delete from Recording Service
curl -X DELETE http://localhost:8081/api/recordings/1
```

**What happens:**
1. ✅ Recording deleted from PostgreSQL
2. ✅ File deleted from MinIO
3. ✅ Kafka event published to `recording.deleted` topic
4. ✅ Search Service consumes event
5. ✅ Recording deleted from MongoDB

**Verify deletion in Search Service:**
```bash
curl http://localhost:8082/api/search/recordings/1
# Should return 404 Not Found
```

✅ **Data stays synchronized!**

---

## API Endpoints Summary

### Recording Service (8081)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/recordings/upload` | Upload audio file |
| GET | `/api/recordings` | Get all recordings |
| GET | `/api/recordings/{id}` | Get recording by ID |
| DELETE | `/api/recordings/{id}` | Delete recording |

### Search Service (8082) - NEW!
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/search/health` | Health check |
| GET | `/api/search/recordings` | Get all recordings |
| GET | `/api/search/recordings/{id}` | Get recording by ID |
| GET | `/api/search/recordings/by-name?fileName=xyz` | Search by file name |
| GET | `/api/search/recordings/by-extension?extension=mp3` | Search by extension |
| GET | `/api/search/recordings/by-duration?min=60&max=300` | Search by duration |
| GET | `/api/search/recordings/by-date?start=...&end=...` | Search by date |
| GET | `/api/search/recordings/by-size?min=...&max=...` | Search by size |
| GET | `/api/search/recordings/complex?...` | Complex search |
| GET | `/api/search/stats` | Get statistics |

---

## Data Flow Example

### Upload Flow:
```
1. User uploads file
   ↓
2. Recording Service (8081)
   - Saves to PostgreSQL
   - Stores file in MinIO
   - Publishes Kafka event
   ↓
3. Kafka Topic: recording.created
   ↓
4. Search Service (8082)
   - Consumes event
   - Saves to MongoDB
```

### Search Flow:
```
1. User searches
   ↓
2. Search Service (8082)
   - Queries MongoDB
   - Returns results
   (Recording Service not involved!)
```

---

## What You Just Learned

✅ **Microservices Architecture** - Multiple independent services  
✅ **Event-Driven Communication** - Services communicate via Kafka  
✅ **Polyglot Persistence** - PostgreSQL + MongoDB  
✅ **Eventual Consistency** - Data syncs asynchronously  
✅ **Service Independence** - Each service can scale separately  
✅ **Kafka Consumers** - Listening to events  
✅ **MongoDB Repositories** - NoSQL data access  
✅ **Multi-Port Services** - 8081, 8082  
✅ **Domain Separation** - Transactional vs Search  

---

## Architecture Patterns Used

### 1. **Event-Driven Architecture**
Services communicate via events, not direct API calls.

### 2. **CQRS (Command Query Responsibility Segregation)**
- Recording Service = Commands (write operations)
- Search Service = Queries (read operations)

### 3. **Database Per Service**
Each service has its own database.

### 4. **Eventual Consistency**
Data syncs asynchronously - there's a small delay.

### 5. **Saga Pattern** (Basic form)
Recording creation spans multiple services coordinated by events.

---

## MongoDB vs PostgreSQL

### Data Comparison:

**PostgreSQL (Recording Service):**
```sql
SELECT * FROM recordings;
```
```
id | file_name              | file_path  | duration | ...
1  | SoundHelix-Song-1.mp3 | uuid.mp3   | 185      | ...
```

**MongoDB (Search Service):**
```javascript
db.recordings.find()
```
```json
{
  "_id": "65abc123...",
  "recordingId": 1,
  "fileName": "SoundHelix-Song-1.mp3",
  "fileNameLower": "soundhelix-song-1.mp3",
  "fileExtension": "mp3",
  ...
}
```

**Notice:**
- MongoDB has `_id` (MongoDB's ID)
- MongoDB has `recordingId` (reference to PostgreSQL)
- MongoDB has denormalized fields (`fileNameLower`, `fileExtension`)

---

## Troubleshooting

### Search Service can't connect to MongoDB?

**Check MongoDB is running:**
```bash
docker logs audiovault-mongodb
```

**Test connection:**
```bash
docker exec -it audiovault-mongodb mongosh -u admin -p admin123
```

---

### Events not being consumed?

**Check Kafka topics:**
```bash
docker exec -it audiovault-kafka kafka-topics --list --bootstrap-server localhost:9092
```

**Check consumer group:**
```bash
docker exec -it audiovault-kafka kafka-consumer-groups \
  --bootstrap-server localhost:9092 \
  --group search-service-group \
  --describe
```

---

### Search Service logs show errors?

**Check logs:**
```bash
# In search-service terminal
# Look for errors like:
# - MongoDB connection refused
# - Kafka consumer errors
# - Deserialization errors
```

---

## View MongoDB Data Directly

**Connect to MongoDB:**
```bash
docker exec -it audiovault-mongodb mongosh -u admin -p admin123
```

**Inside MongoDB shell:**
```javascript
use audiovault_search

// See all recordings
db.recordings.find().pretty()

// Count recordings
db.recordings.count()

// Search by file name
db.recordings.find({ fileName: /soundhelix/i })

// Exit
exit
```

---

## Project Structure

```
audio-vault/
├── docker-compose.yml                    # All 5 services
├── recording-service/                    # Service 1 (8081)
│   ├── src/main/java/com/audiovault/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   └── kafka/                        # Kafka producer
│   └── pom.xml
└── search-service/                       # Service 2 (8082) - NEW!
    ├── src/main/java/com/audiovault/search/
    │   ├── controller/
    │   │   └── SearchController.java
    │   ├── service/
    │   │   └── SearchService.java
    │   ├── repository/
    │   │   └── RecordingSearchRepository.java
    │   ├── consumer/
    │   │   └── RecordingEventConsumer.java  # Kafka consumer
    │   ├── model/
    │   │   └── RecordingDocument.java       # MongoDB document
    │   └── dto/
    │       └── RecordingEvent.java
    └── pom.xml
```

---

## Next Steps (Phase 9)

Phase 9 will add **React Frontend**:
- User interface for uploads
- Display recordings
- Search interface
- Audio player
- Material-UI components

---

## Commit Message Suggestion

```bash
git add .
git commit -m "Phase 8: Add Search Microservice with MongoDB

- Create separate search-service Spring Boot application
- Add MongoDB to docker-compose.yml
- Implement Kafka consumer for recording events
- Create RecordingDocument model for MongoDB
- Add RecordingSearchRepository with Spring Data MongoDB
- Implement SearchService with advanced query methods
- Create SearchController with 10 search endpoints
- Enable full-text search with text indexes
- Support search by: name, extension, duration, date, size
- Add statistics endpoint
- Event-driven data synchronization via Kafka
- Polyglot persistence: PostgreSQL + MongoDB
- True microservices architecture with independent services"

git push origin main
```

---

## Verification Checklist

Before moving to Phase 9, verify:
- ✅ MongoDB container running
- ✅ Recording Service on port 8081
- ✅ Search Service on port 8082
- ✅ Can upload file to Recording Service
- ✅ Search Service receives Kafka event
- ✅ Recording appears in MongoDB
- ✅ Can search from Search Service
- ✅ Can delete from Recording Service
- ✅ Deletion syncs to Search Service

---

**You now have a real microservices architecture!** 🎉

Two independent services, communicating via events, using different databases. This is exactly how companies like Netflix, Uber, and Amazon build systems.

Ready to test Phase 8? 🚀