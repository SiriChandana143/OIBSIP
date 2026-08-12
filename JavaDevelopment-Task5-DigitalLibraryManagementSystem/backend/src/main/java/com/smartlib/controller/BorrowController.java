package com.smartlib.controller;

import com.smartlib.dto.*;
import com.smartlib.service.BorrowService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
@Tag(name = "Borrow", description = "Book borrowing and return APIs")
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping
    @Operation(summary = "Borrow a book")
    public ResponseEntity<ApiResponse<BorrowResponse>> borrowBook(@RequestBody Map<String, Long> request) {
        Long userId = request.get("userId");
        Long bookId = request.get("bookId");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book borrowed successfully", borrowService.borrowBook(userId, bookId)));
    }

    @PutMapping("/return/{borrowId}")
    @Operation(summary = "Return a borrowed book")
    public ResponseEntity<ApiResponse<BorrowResponse>> returnBook(@PathVariable Long borrowId) {
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully", borrowService.returnBook(borrowId)));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get borrow history for a user")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getUserBorrows(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(borrowService.getUserBorrows(userId)));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(summary = "Get all active borrows")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getActiveBorrows() {
        return ResponseEntity.ok(ApiResponse.success(borrowService.getActiveBorrows()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'LIBRARIAN')")
    @Operation(summary = "Get all borrow records")
    public ResponseEntity<ApiResponse<List<BorrowResponse>>> getAllBorrows() {
        return ResponseEntity.ok(ApiResponse.success(borrowService.getAllBorrows()));
    }
}
