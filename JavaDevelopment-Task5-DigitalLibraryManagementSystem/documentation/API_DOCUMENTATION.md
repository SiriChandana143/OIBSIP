# SMARTLIB AI - API Documentation

Base URL: `http://localhost:8080/api`

Authentication: Bearer JWT token in `Authorization` header.

---

## Authentication

### POST /auth/register
Register a new user account.

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "phone": "9876543210"
}
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbG...",
    "userId": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
  }
}
```

### POST /auth/login
Authenticate and receive JWT token.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

---

## Books

### GET /books
Get all books. **Auth required.**

### GET /books/search?query={term}&userId={id}
Search books by title, author, ISBN, or category.

### GET /books/{id}
Get book details by ID.

### POST /books (Admin)
Add a new book.

**Request Body:**
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "categoryId": 1,
  "publisher": "Prentice Hall",
  "publicationYear": 2008,
  "quantity": 5,
  "imageUrl": "https://..."
}
```

### PUT /books/{id} (Admin)
Update book details.

### DELETE /books/{id} (Admin)
Delete a book.

---

## Borrow

### POST /borrow
Borrow a book.

**Request Body:**
```json
{ "userId": 1, "bookId": 3 }
```

### PUT /borrow/return/{borrowId}
Return a borrowed book. Auto-calculates fines if overdue.

### GET /borrow/user/{userId}
Get user's borrow history.

### GET /borrow/active (Admin/Librarian)
Get all active borrows.

---

## Recommendations

### GET /recommendations/{userId}
Get AI-powered personalized book recommendations.

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 5,
      "title": "Deep Learning",
      "author": "Ian Goodfellow",
      "category": "Machine Learning",
      "available": true
    }
  ]
}
```

---

## Chatbot

### POST /chat
Chat with AI library assistant.

**Request Body:**
```json
{
  "message": "Find machine learning books",
  "userId": 1
}
```

**Response:**
```json
{
  "success": true,
  "data": {
    "reply": "I found 3 book(s) related to \"machine learning\":",
    "suggestedBooks": [...]
  }
}
```

---

## Admin

### GET /admin/analytics
Library statistics dashboard data.

### GET /admin/users
List all users.

### PUT /admin/users/{id}/role
Change user role. Body: `{ "role": "LIBRARIAN" }`

### PUT /admin/users/{id}/toggle-status
Block/unblock user.

### GET /admin/fines
List all fines.

### PUT /admin/fines/{id}/pay
Mark fine as paid.

---

## Error Responses

```json
{
  "success": false,
  "message": "Error description"
}
```

| Status | Meaning |
|--------|---------|
| 400 | Bad Request / Validation Error |
| 401 | Unauthorized |
| 403 | Forbidden (Insufficient Role) |
| 404 | Resource Not Found |
| 500 | Internal Server Error |
