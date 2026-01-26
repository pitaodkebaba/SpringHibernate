# Music Library Management System

A full-stack desktop application for managing a music library, creating playlists, and categorizing songs by genres. The project features a **Spring Boot** backend serving a REST API, secured with JWT authentication. The entire system is containerized using **Docker**.

## Features

* **User Authentication:** Secure user registration and login using JWT (JSON Web Tokens).
* **Role-based Access:** Differentiates between regular users and administrators.
* **Music Management:**
  * Add, view, and manage **Songs**.
  * Categorize tracks by **Genres**.
  * Create and manage personal **Playlists**.
* **RESTful API:** fully documented with OpenAPI/Swagger.
* **Dockerized Environment:** Easy setup with `docker-compose` for backend and database services.

## Tech Stack

**Backend:**
* Java (Spring Boot)
* Spring Security (JWT)
* Spring Data JPA / Hibernate
* OpenAPI / Swagger for API documentation
* Maven (Build tool)

**Database & DevOps:**
* SQL Database (Dockerized)
* Docker & Docker Compose
* JUnit 5 & Mockito (Unit Testing)

## Project Structure

```text
ProjektJava/
├── project/
│   ├── src/main/java/.../Backend/    # Spring Boot REST API
│   ├── src/main/java/.../Frontend/   # JavaFX Application
│   ├── src/main/resources/           # Configuration and FXML views
│   └── src/test/java/.../UnitTests/  # Backend Unit Tests
├── Database/                         # SQL initialization scripts and DB config
├── backend.Dockerfile                # Docker image configuration for the backend
├── database.Dockerfile               # Docker image configuration for the database
└── docker-compose.yml                # Docker orchestration file
```

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- Java Development Kit (JDK) 17 or higher

- Maven (or use the included mvnw wrapper)

- Docker and Docker Compose

## How to Run the Project

### Method 1: The Docker Way (Recommended)

You can run the entire backend and database infrastructure with a single command using Docker.

Open a terminal in the root directory (ProjektJava).

Run the following command:

```Bash
docker-compose up --build
```
The Database and Spring Boot backend will start automatically.

### Method 2: Running Locally (For Development)

1. Start the Database: You can start just the database using Docker:

```Bash
docker-compose up db
```
(Make sure to configure your application.properties or .env file with the correct database credentials).

2. Start the Backend (Spring Boot): Navigate to the project directory and run:

```Bash
cd project
./mvnw spring-boot:run
```

## API Documentation (Swagger)

Once the backend is running, you can access the interactive API documentation provided by OpenAPI/Swagger at:

http://localhost:8080/swagger-ui.html (or the port specified in your configuration).

## Testing

The project includes comprehensive unit tests for the backend services (Authentication, Genre, Playlist, Song, and User services).

To run the tests, use Maven:

```Bash
cd project
./mvnw test
```

## Environment Variables

The project uses .env files for configuration. Make sure to check the project/.env and Database/.env files to configure your:

Database credentials (URL, username, password)

JWT Secret Key

Server Port

📝 License
This project was created for educational purposes.
