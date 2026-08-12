# UML Diagrams - SMARTLIB AI

## 1. Use Case Diagram

```mermaid
graph TB
    subgraph Actors
        U[User]
        L[Librarian]
        A[Admin]
    end

    subgraph UserModule["User Module"]
        UC1[Register / Login]
        UC2[Search Books]
        UC3[Borrow / Return Books]
        UC4[Reserve Books]
        UC5[View Recommendations]
        UC6[Use AI Chatbot]
        UC7[Manage Profile]
        UC8[View Fines]
    end

    subgraph AdminModule["Admin Module"]
        UC9[Manage Books]
        UC10[Manage Users]
        UC11[View Analytics]
        UC12[Manage Fines]
    end

    subgraph LibrarianModule["Librarian Module"]
        UC13[Issue Books]
        UC14[Process Returns]
    end

    U --> UC1 & UC2 & UC3 & UC4 & UC5 & UC6 & UC7 & UC8
    A --> UC9 & UC10 & UC11 & UC12 & UC13 & UC14
    L --> UC13 & UC14 & UC2
```

## 2. Class Diagram (Core Domain)

```mermaid
classDiagram
    class User {
        +Long id
        +String name
        +String email
        +String password
        +String phone
        +Role role
        +LocalDateTime createdDate
    }

    class Book {
        +Long id
        +String title
        +String author
        +String isbn
        +String category
        +int quantity
        +int availableQuantity
    }

    class BorrowRecord {
        +Long id
        +LocalDateTime issueDate
        +LocalDateTime dueDate
        +LocalDateTime returnDate
        +BorrowStatus status
    }

    class Reservation {
        +Long id
        +LocalDateTime reservationDate
        +ReservationStatus status
    }

    class Fine {
        +Long id
        +BigDecimal amount
        +PaymentStatus paymentStatus
    }

    class Recommendation {
        +Long id
        +Double recommendationScore
    }

    User "1" --> "*" BorrowRecord
    Book "1" --> "*" BorrowRecord
    User "1" --> "*" Reservation
    Book "1" --> "*" Reservation
    User "1" --> "*" Fine
    BorrowRecord "1" --> "0..1" Fine
    User "1" --> "*" Recommendation
    Book "1" --> "*" Recommendation
```

## 3. Sequence Diagram - Borrow Book

```mermaid
sequenceDiagram
    actor User
    participant FE as React Frontend
    participant API as BorrowController
    participant SVC as BorrowService
    participant DB as MySQL

    User->>FE: Click Borrow
    FE->>API: POST /api/borrow
    API->>SVC: borrowBook(userId, bookId)
    SVC->>DB: Check availability
    alt Available
        SVC->>DB: Create borrow record
        SVC->>DB: Decrement available quantity
        SVC-->>API: BorrowResponse
        API-->>FE: 201 Created
        FE-->>User: Success toast
    else Not available
        SVC-->>API: BadRequestException
        API-->>FE: 400 Error
        FE-->>User: Error message
    end
```

## 4. Component Diagram

```mermaid
graph LR
    subgraph Frontend
        Pages[React Pages]
        Context[Auth Context]
        APIClient[Axios API Client]
    end

    subgraph Backend
        Controllers[REST Controllers]
        Services[Business Services]
        Repos[JPA Repositories]
        Security[JWT Security]
        AI[AI Modules]
    end

    subgraph Database
        MySQL[(MySQL 8)]
    end

    Pages --> Context
    Pages --> APIClient
    APIClient -->|JWT| Controllers
    Controllers --> Security
    Controllers --> Services
    Services --> Repos
    Services --> AI
    Repos --> MySQL
```

## 5. Activity Diagram - Recommendation Engine

```mermaid
flowchart TD
    A[Get User ID] --> B[Load borrow history]
    B --> C[Load search history]
    C --> D[Extract preferred categories & authors]
    D --> E[Score all available books]
    E --> F{Already borrowed?}
    F -->|Yes| G[Skip book]
    F -->|No| H[Calculate similarity score]
    G --> E
    H --> I[Sort by score descending]
    I --> J[Return top N recommendations]
```
