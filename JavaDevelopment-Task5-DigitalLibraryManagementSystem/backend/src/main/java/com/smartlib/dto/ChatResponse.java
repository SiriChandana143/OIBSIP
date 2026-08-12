package com.smartlib.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatResponse {

    private String reply;
    private List<BookResponse> suggestedBooks;
}
