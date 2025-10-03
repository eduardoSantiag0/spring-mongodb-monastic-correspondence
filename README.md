The aim of this project is to study Logging and CRUD operations with Mongo DB.

# 📜 Monastic Correspondence

## 🕰️ Context

After the collapse of civilization, monks from the Order of Saint Leibowitz dedicate themselves to preserving relics and records of the old world. They write and exchange **letters** between abbots, scribes, and explorers who scour the ruins of the past.


This project implements a system to **register, store, and query these letters** circulating among the monks — while logging every action for audit and historical legacy purposes.

The ideia for the project came by my reading of _A Canticle for Leibowitz_ 

---

## 🛠️ Technologies Used

- Spring Boot (Java 17)
- MongoDB
- SLF4J + Logback
- Swagger 
- Docker Compose (to launch MongoDB container)

---

## 🚀 Features

- **Create letters** representing monastic correspondence.
- **Query letters** by sender, receiver, approximate year, keyword, or state.
- **Retrieve versions** of the same letter, supporting historical audit trails.
- **Update letter content or state**, with version increment and old version archiving.
- **Delete letters** with logging.
- **Simulate deprecated content** and provide a mechanism to fix deprecated letters.
- **Comprehensive logging** of actions like creation, reading, editing, deletion, and fixing.
- **Add and list comments** on a letter, with a snapshot of the **last comment** kept on the letter document (for fast reads).

---

## 🏗️ Architecture Overview

### Main REST Controllers

- `LettersController`
    - POST `/monastic-correspondence` — Create a new letter
    - GET `/monastic-correspondence` — Filter letters by parameters (name, date, keyword, state)
    - GET `/monastic-correspondence/{id}` — Retrieve a letter by ID
    - DELETE `/monastic-correspondence/{id}` — Delete a letter
    - PATCH `/monastic-correspondence/{id}` — Fix deprecated letter content
    - PATCH `/monastic-correspondence/update/{id}` — Update letter state or content

- `LettersVersionsController`
    - GET `/versions/of/{originalId}` — List versions of the same letter

- `CommentController` 
  - `POST /monastic-correspondence/{id}/comment` — Add a new comment to the letter `{id}`
  - `GET /monastic-correspondence/{id}/comment?page={page}&size={size}` — List comments of letter `{id}`


### Core Services

- `LettersService` — Implements business logic for creating, updating, deleting, fixing letters, and filtering queries.
- `VersionServices` — Manages saving old versions of letters for audit/history.
- `CommentService` — Adds and queries comments; updates letter’s last comment.

### Data Model

- `LettersEntity` — Represents the current letter in the "letters" MongoDB collection.
- `LetterVersion` — Represents archived versions of letters in the "letter_versions" MongoDB collection.
- `CommentDocument` — Stores comments in a dedicated collection; letters keep only the latest comment.

### Versioning & Deprecation Simulation

- Letters can be marked as `DEPRECATED` with simulated obfuscated content.
- Fixing a letter restores the original content and changes its state to `READABLE`.
- Each update increments a version counter, and previous versions are archived.

---


### 📝 Logging
All significant actions like creation, update, deletion, reading, fixing and commenting are logged using SLF4J + Logback with contextual details (letter IDs, version numbers, authors, states).


---

## 📦 Build and Run

### Prerequisites

- Java 17+
- Maven
- Docker and Docker Compose (for MongoDB)

### Running with Docker Compose

1. Start MongoDB container:

```bash
  docker-compose up -d
```

2. Build and run the Spring Boot application:

```bash
    mvn clean install
    mvn spring-boot:run
```

### API Documentation

Swagger UI is available at:
```bash
  http://localhost:8080/swagger-ui.html
```


