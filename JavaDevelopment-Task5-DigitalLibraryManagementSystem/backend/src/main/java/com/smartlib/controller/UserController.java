package com.smartlib.controller;

import com.smartlib.dto.*;
import com.smartlib.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "User profile and activity APIs")
public class UserController {

    private final UserService userService;
    private final FineService fineService;
    private final ReservationService reservationService;
    private final RecommendationService recommendationService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
    }

    @PutMapping("/{id}/profile")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable Long id, @RequestBody Map<String, String> request) {
        return ResponseEntity.ok(ApiResponse.success("Profile updated",
                userService.updateProfile(id, request.get("name"), request.get("phone"))));
    }

    @GetMapping("/{id}/fines")
    @Operation(summary = "Get user fines")
    public ResponseEntity<ApiResponse<List<FineResponse>>> getUserFines(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(fineService.getUserFines(id)));
    }

    @GetMapping("/{id}/notifications")
    @Operation(summary = "Get user notifications")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUserNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getUserNotifications(id)));
    }

    @PostMapping("/reservations")
    @Operation(summary = "Create a book reservation")
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(
            @RequestBody Map<String, Long> request) {
        return ResponseEntity.ok(ApiResponse.success("Reservation created",
                reservationService.createReservation(request.get("userId"), request.get("bookId"))));
    }

    @GetMapping("/{id}/reservations")
    @Operation(summary = "Get user reservations")
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getUserReservations(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(reservationService.getUserReservations(id)));
    }

    @DeleteMapping("/reservations/{id}")
    @Operation(summary = "Cancel a reservation")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled", null));
    }
}

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "AI recommendation APIs")
class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/{userId}")
    @Operation(summary = "Get personalized book recommendations")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getRecommendations(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.success(recommendationService.getRecommendations(userId)));
    }
}

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "AI Library Assistant APIs")
class ChatController {

    private final ChatbotService chatbotService;

    @PostMapping
    @Operation(summary = "Chat with AI library assistant")
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest request) {
        return ResponseEntity.ok(ApiResponse.success(chatbotService.processMessage(request)));
    }
}
