# SMARTLIB AI - Project Report

## 1. Introduction

**Project Title:** SMARTLIB AI — Intelligent Digital Library Management System

**Objective:** To design and implement a full-stack, AI-powered digital library management system that automates book cataloging, borrowing, reservations, fine calculation, and provides intelligent recommendations and an AI assistant for users.

**Target Users:** Library administrators, librarians, and registered library members.

---

## 2. Problem Statement

Traditional library management relies on manual record-keeping, which leads to:

- Difficulty tracking issued and overdue books
- Manual fine calculation errors
- No personalized book discovery for users
- Limited self-service for members

SMARTLIB AI addresses these problems with a modern web application featuring role-based access, automated workflows, and AI-driven features.

---

## 3. System Requirements

### Functional Requirements

| Module | Requirements |
|--------|-------------|
| Authentication | Registration, login, JWT tokens, role-based access (ADMIN, LIBRARIAN, USER) |
| Book Management | CRUD operations, search, availability tracking |
| Borrow/Return | Issue books, set due dates, process returns, calculate fines (₹5/day) |
| Reservations | Waiting list for unavailable books |
| AI Recommendations | Content-based suggestions from history and search patterns |
| AI Chatbot | Natural language queries for books, dues, fines, recommendations |
| Admin Analytics | Dashboard with pie, bar, and line charts |
| Profile | Users can update name and phone |

### Non-Functional Requirements

- Responsive UI (mobile-friendly)
- RESTful API with Swagger documentation
- Docker-based deployment
- Unit test coverage for core services
- Secure password storage (BCrypt)

---

## 4. Technology Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 21, Spring Boot 3.2, Spring Security, JWT |
| Frontend | React 18, Tailwind CSS, Chart.js, Vite |
| Database | MySQL 8 |
| DevOps | Docker, Docker Compose |
| Testing | JUnit 5, Mockito |

---

## 5. System Design

### Architecture

The system follows a **3-tier layered architecture**:

1. **Presentation Layer** — React SPA with protected routes
2. **Business Layer** — Spring Boot services (Auth, Book, Borrow, Recommendation, Chatbot, Analytics)
3. **Data Layer** — Spring Data JPA repositories with MySQL

See [ARCHITECTURE.md](./ARCHITECTURE.md) and [ER_DIAGRAM.md](./ER_DIAGRAM.md) for detailed diagrams.

### Database Schema

Nine normalized tables: `users`, `books`, `categories`, `borrow_records`, `reservations`, `fines`, `notifications`, `search_history`, `recommendations`.

---

## 6. AI Modules

### 6.1 Recommendation Engine

**Type:** Content-based filtering

**Inputs:**
- User borrowing history (categories, authors)
- Search history queries
- Book metadata (category, author, title keywords)

**Algorithm:**
1. Build a preference profile from past activity
2. Score each available book by category/author/title match
3. Exclude already-borrowed books
4. Return top-scoring books

### 6.2 Library Chatbot

**Type:** Rule-based NLP assistant

**Capabilities:**
- Search books by keyword
- Show borrowed books and due dates
- Display pending fines
- Trigger recommendation queries
- Answer common library FAQs

---

## 7. Implementation Highlights

### Security
- JWT stateless authentication
- Role-based endpoint protection (`@PreAuthorize`)
- CORS configured for frontend origins

### Key Features Delivered
- Admin dashboard with real-time analytics and monthly borrow trends
- Librarian issue/return management panel
- User profile management page
- Book details modal with full metadata
- Automatic fine generation on overdue returns
- Reservation queue with auto-fulfillment on return

---

## 8. Testing

Unit tests implemented for:
- `AuthService` — registration and login
- `BookService` — CRUD and search
- `BorrowService` — borrow, return, fine calculation
- `RecommendationService` — scoring logic

Run tests:
```bash
cd backend
mvn test
```

---

## 9. Deployment

### Docker (Recommended)
```bash
docker-compose up --build
```

Services:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

### Default Accounts
| Role | Email | Password |
|------|-------|----------|
| Admin | admin@smartlib.ai | admin123 |
| Librarian | librarian@smartlib.ai | lib123 |

---

## 10. Future Enhancements

- Email/SMS notifications for due dates
- Real ML model integration (collaborative filtering)
- PDF/eBook support
- Payment gateway for online fine payment
- Mobile app (React Native)

---

## 11. Conclusion

SMARTLIB AI is a production-ready digital library management system demonstrating full-stack development, clean architecture, AI feature integration, and deployment readiness. It serves as a comprehensive BTech major project suitable for portfolio showcase and internship applications.

---

## 12. References

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- React Documentation: https://react.dev
- Chart.js Documentation: https://www.chartjs.org
- JWT RFC 7519: https://tools.ietf.org/html/rfc7519
