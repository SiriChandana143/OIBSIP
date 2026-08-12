# SMARTLIB AI - System Architecture

## Overview

SMARTLIB AI follows a **3-tier layered architecture** with clear separation of concerns.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                     │
│  ┌─────────────────────────────────────────────────┐    │
│  │           React.js SPA (Port 3000)              │    │
│  │  Landing │ Login │ Dashboard │ Admin │ Chatbot  │    │
│  └──────────────────────┬──────────────────────────┘    │
└─────────────────────────┼───────────────────────────────┘
                          │ HTTP/REST + JWT
┌─────────────────────────┼───────────────────────────────┐
│                    APPLICATION LAYER                      │
│  ┌──────────────────────┴──────────────────────────┐    │
│  │         Spring Boot REST API (Port 8080)         │    │
│  │                                                    │    │
│  │  ┌──────────┐  ┌──────────┐  ┌───────────────┐  │    │
│  │  │Controller│─▶│ Service  │─▶│  Repository   │  │    │
│  │  └──────────┘  └──────────┘  └───────┬───────┘  │    │
│  │                                        │           │    │
│  │  ┌──────────────┐  ┌────────────────┐ │           │    │
│  │  │   Security   │  │  AI Modules    │ │           │    │
│  │  │  JWT + RBAC  │  │ Rec + Chatbot  │ │           │    │
│  │  └──────────────┘  └────────────────┘ │           │    │
│  └────────────────────────────────────────┼───────────┘    │
└───────────────────────────────────────────┼───────────────┘
                                            │ JPA/Hibernate
┌───────────────────────────────────────────┼───────────────┐
│                      DATA LAYER                            │
│  ┌────────────────────────────────────────┴──────────┐    │
│  │              MySQL 8.0 Database                      │    │
│  │  users │ books │ categories │ borrow_records │ ...  │    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### Controller Layer
- Handles HTTP requests/responses
- Input validation
- Route mapping
- Swagger documentation

### Service Layer
- Business logic
- Transaction management
- AI recommendation engine
- Chatbot intent processing

### Repository Layer
- Database CRUD operations
- Custom queries
- JPA entity management

### Security Layer
- JWT token generation/validation
- BCrypt password encryption
- Role-based access control (ADMIN, LIBRARIAN, USER)

## Design Patterns Used
- **Layered Architecture** - Separation of concerns
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - Data transfer between layers
- **Strategy Pattern** - Chatbot intent handlers
- **Factory Pattern** - JWT token creation

## Deployment Architecture (Docker)

```
┌─────────────┐  ┌─────────────┐  ┌─────────────┐
│  Frontend   │  │   Backend   │  │    MySQL    │
│  (Nginx)    │─▶│ Spring Boot │─▶│  Database   │
│  Port 3000  │  │  Port 8080  │  │  Port 3306  │
└─────────────┘  └─────────────┘  └─────────────┘
     Docker           Docker           Docker
```
