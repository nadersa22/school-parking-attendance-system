# School Parking & Attendance Management System

A full-stack application for managing teacher parking reservations and attendance in a school environment.

Teachers can reserve available parking spots, check in and out, and review their reservation and attendance history. Administrators can manage teachers, parking spots, attendance records, and daily reports.

## Features

### Teacher

- Register and log in securely
- View available parking spots
- Reserve one parking spot at a time
- Check in using an active reservation
- Check out and calculate attendance duration
- View personal reservation history
- View personal attendance history

### Administrator

- Add and remove teachers
- Create, update, and delete parking spots
- View all attendance records
- View daily attendance and parking reports
- Monitor available, reserved, and occupied spots

## Business Rules

- A reservation expires after 15 minutes if the teacher does not check in.
- A teacher can only have one active reservation.
- A parking spot can be available, reserved, or occupied.
- Attendance under four hours is marked `INVALID`.
- Attendance of four hours or more is marked `VALID`.
- Only administrators can manage teachers and parking spots.

## Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT authentication
- PostgreSQL
- Maven
- Lombok

### Frontend

- React
- TypeScript
- Vite
- React Router
- Axios

## Project Structure

```text
school-parking-attendance-system/
├── backend/
│   ├── src/main/java/
│   │   └── com/school/parkingattendance/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── entity/
│   │       ├── exception/
│   │       ├── repository/
│   │       ├── security/
│   │       └── service/
│   └── pom.xml
└── frontend/
    ├── src/
    │   ├── api/
    │   ├── components/
    │   ├── pages/
    │   ├── routes/
    │   └── types/
    └── package.json
```

## Backend Setup

### Requirements

- Java 17 or later
- PostgreSQL
- Maven, or the included Maven wrapper

### Configuration

Create this file:

```text
backend/src/main/resources/application.properties
```

Use `application-example.properties` as a reference:

```properties
spring.application.name=parking-attendance

spring.datasource.url=jdbc:postgresql://localhost:5432/school_parking_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

The JWT signing secret must be provided through the `JWT_SECRET` environment variable. Use a long random value of at least 32 characters.

On Windows PowerShell:

```powershell
$env:JWT_SECRET="replace-with-a-long-random-secret"
cd backend
.\mvnw.cmd spring-boot:run
```

The backend will run at:

```text
http://localhost:8080
```

## Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

The frontend will run at:

```text
http://localhost:5173
```

## Main API Endpoints

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public |
| GET | `/api/auth/me` | Admin, Teacher |
| GET | `/api/parking-spots` | Admin, Teacher |
| GET | `/api/parking-spots/available` | Admin, Teacher |
| POST | `/api/parking-spots` | Admin |
| PUT | `/api/parking-spots/{id}` | Admin |
| DELETE | `/api/parking-spots/{id}` | Admin |
| POST | `/api/reservations/me` | Admin, Teacher |
| GET | `/api/reservations/me` | Admin, Teacher |
| POST | `/api/attendance/me/check-in` | Admin, Teacher |
| POST | `/api/attendance/me/check-out` | Admin, Teacher |
| GET | `/api/attendance/me` | Admin, Teacher |
| GET | `/api/attendance` | Admin |
| GET | `/api/reports/daily` | Admin |

## Security

- Passwords are hashed using BCrypt.
- Authentication uses signed JWT access tokens.
- JWT secrets are loaded from an environment variable.
- Role-based authorization separates administrator and teacher operations.
- Teacher-specific operations use the authenticated user ID from the token.
- Local database configuration files are excluded from Git.

## Quality Checks

Backend:

```powershell
cd backend
.\mvnw.cmd test
```

Frontend:

```bash
cd frontend
npm run build
npm run lint
```

## Author

Nader Serhal  
Software Developer
