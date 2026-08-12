# SMARTLIB AI - Intelligent Digital Library Management System

A production-quality, full-stack AI-powered Digital Library Management System built with Spring Boot and React.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![React](https://img.shields.io/badge/React-18-blue)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)

## Features

### User Features
- Registration & JWT Authentication
- Book Search (title, author, ISBN, category)
- Borrow & Return books with automatic due dates
- Book Reservations (waiting list)
- AI-powered personalized recommendations
- AI Library Chatbot assistant
- Fine tracking (₹5/day overdue)
- Borrow history & profile management

### Admin Features
- Analytics dashboard with charts (Pie, Bar)
- Book CRUD management
- User management (roles, block/unblock)
- Issue tracking & fine management
- Real-time library statistics

### AI Modules
- **Content-Based Recommendation Engine** - Analyzes borrowing history, search patterns, categories, and authors
- **Intelligent Chatbot** - Natural language queries for books, due dates, fines, and recommendations

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.2, Spring Security, JWT |
| Frontend | React 18, Tailwind CSS, Chart.js |
| Database | MySQL 8 |
| ORM | Hibernate / Spring Data JPA |
| API Docs | Swagger / OpenAPI 3 |
| Testing | JUnit 5, Mockito |
| DevOps | Docker, Docker Compose |

## Project Structure

```
SMARTLIB-AI/
├── backend/                 # Spring Boot REST API
│   ├── src/main/java/com/smartlib/
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business Logic
│   │   ├── repository/      # Data Access
│   │   ├── entity/          # JPA Entities
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── security/        # JWT & Security
│   │   ├── config/          # Configuration
│   │   └── exception/       # Error Handling
│   └── src/test/            # Unit Tests
├── frontend/                # React SPA
│   └── src/
│       ├── pages/           # Page Components
│       ├── components/      # Reusable Components
│       ├── context/         # Auth Context
│       └── services/        # API Service
├── database/                # SQL Schema
├── documentation/           # Project Docs
│   ├── ARCHITECTURE.md
│   ├── ER_DIAGRAM.md
│   ├── API_DOCUMENTATION.md
│   ├── UML_DIAGRAM.md
│   └── PROJECT_REPORT.md
└── docker-compose.yml       # Docker Orchestration
```

## Quick Start

### Prerequisites
- Java 21+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+
- (Optional) Docker & Docker Compose

### Option 1: Docker (Recommended)

```bash
docker-compose up --build
```

Access:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

### Option 2: Manual Setup

**1. Database**
```bash
mysql -u root -p < database/schema.sql
```

**2. Backend**
```bash
cd backend
mvn spring-boot:run
```

**3. Frontend**
```bash
cd frontend
npm install
npm run dev
```

Access frontend at http://localhost:3000

## Default Accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@smartlib.ai | admin123 |
| Librarian | librarian@smartlib.ai | lib123 |

Register a new user account for the USER role.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register user |
| POST | /api/auth/login | Login & get JWT |
| GET | /api/books | List all books |
| GET | /api/books/search?query= | Search books |
| POST | /api/books | Add book (Admin) |
| POST | /api/borrow | Borrow a book |
| PUT | /api/borrow/return/{id} | Return a book |
| GET | /api/recommendations/{userId} | Get AI recommendations |
| POST | /api/chat | AI chatbot |
| GET | /api/admin/analytics | Dashboard analytics |

Full API documentation available at `/swagger-ui.html` when backend is running.

## Running Tests

```bash
cd backend
mvn test
```

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌────────────┐
│   React     │────▶│  Spring Boot │────▶│   MySQL    │
│  Frontend   │ JWT │   REST API   │ JPA │  Database  │
└─────────────┘     └──────────────┘     └────────────┘
                           │
                    ┌──────┴──────┐
                    │  AI Modules │
                    │ Rec + Chat  │
                    └─────────────┘
```

## License

This project is created for educational and portfolio purposes.

## Author

Built as a BTech CSE Major Project - SMARTLIB AI Team
