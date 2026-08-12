package com.smartlib.controller;

import com.smartlib.dto.*;
import com.smartlib.entity.Role;
import com.smartlib.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin management APIs")
public class AdminController {

    private final UserService userService;
    private final FineService fineService;
    private final AnalyticsService analyticsService;

    @GetMapping("/users")
    @Operation(summary = "Get all users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
    }

    @PutMapping("/users/{id}/role")
    @Operation(summary = "Change user role")
    public ResponseEntity<ApiResponse<UserResponse>> updateRole(
            @PathVariable Long id, @RequestBody Map<String, String> request) {
        Role role = Role.valueOf(request.get("role"));
        return ResponseEntity.ok(ApiResponse.success("Role updated", userService.updateUserRole(id, role)));
    }

    @PutMapping("/users/{id}/toggle-status")
    @Operation(summary = "Block/unblock user")
    public ResponseEntity<ApiResponse<UserResponse>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", userService.toggleUserStatus(id)));
    }

    @GetMapping("/fines")
    @Operation(summary = "Get all fines")
    public ResponseEntity<ApiResponse<List<FineResponse>>> getAllFines() {
        return ResponseEntity.ok(ApiResponse.success(fineService.getAllFines()));
    }

    @PutMapping("/fines/{id}/pay")
    @Operation(summary = "Mark fine as paid")
    public ResponseEntity<ApiResponse<FineResponse>> markFinePaid(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Fine marked as paid", fineService.markAsPaid(id)));
    }

    @GetMapping("/analytics")
    @Operation(summary = "Get library analytics")
    public ResponseEntity<ApiResponse<AnalyticsResponse>> getAnalytics() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getAnalytics()));
    }
}
