# SMARTLIB AI - Entity Relationship Diagram

## ER Diagram

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   USERS      │       │    BOOKS     │       │  CATEGORIES  │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ PK id        │       │ PK id        │       │ PK id        │
│    name      │       │    title     │       │    category  │
│    email     │       │    author    │       │    _name     │
│    password  │       │    isbn      │       └──────┬───────┘
│    phone     │       │ FK category  │◀─────────────┘
│    role      │       │    _id       │
│    active    │       │    publisher │
│    created   │       │    pub_year  │
│    _date     │       │    language  │
└──────┬───────┘       │    quantity  │
       │               │    avail_qty │
       │               │    image_url │
       │               └──────┬───────┘
       │                      │
       │    ┌─────────────────┼─────────────────┐
       │    │                 │                  │
       ▼    ▼                 ▼                  ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ BORROW       │  │ RESERVATIONS │  │ RECOMMEND    │  │ SEARCH       │
│ _RECORDS     │  │              │  │ _ATIONS      │  │ _HISTORY     │
├──────────────┤  ├──────────────┤  ├──────────────┤  ├──────────────┤
│ PK id        │  │ PK id        │  │ PK id        │  │ PK id        │
│ FK user_id   │  │ FK user_id   │  │ FK user_id   │  │ FK user_id   │
│ FK book_id   │  │ FK book_id   │  │ FK book_id   │  │    search    │
│    issue_date│  │    reserv    │  │    score     │  │    _query    │
│    due_date  │  │    _date     │  │    created   │  │    search    │
│    return    │  │    status    │  │    _date     │  │    _date     │
│    _date     │  └──────────────┘  └──────────────┘  └──────────────┘
│    status    │
└──────┬───────┘
       │
       ▼
┌──────────────┐  ┌──────────────┐
│    FINES     │  │ NOTIFICATIONS│
├──────────────┤  ├──────────────┤
│ PK id        │  │ PK id        │
│ FK user_id   │  │ FK user_id   │
│ FK borrow_id │  │    message   │
│    amount    │  │    created   │
│    payment   │  │    _date     │
│    _status   │  │    status    │
│    created   │  └──────────────┘
│    _date     │
└──────────────┘
```

## Relationships

| Parent | Child | Type | Description |
|--------|-------|------|-------------|
| users | borrow_records | 1:N | User borrows many books |
| books | borrow_records | 1:N | Book borrowed by many users |
| users | reservations | 1:N | User makes many reservations |
| books | reservations | 1:N | Book reserved by many users |
| users | fines | 1:N | User has many fines |
| borrow_records | fines | 1:1 | Borrow may generate one fine |
| users | notifications | 1:N | User receives many notifications |
| users | search_history | 1:N | User has search history |
| users | recommendations | 1:N | User gets many recommendations |
| categories | books | 1:N | Category contains many books |

## Normalization
- **1NF**: All attributes are atomic
- **2NF**: No partial dependencies (all non-key attributes depend on full PK)
- **3NF**: No transitive dependencies (category_name in separate table)
