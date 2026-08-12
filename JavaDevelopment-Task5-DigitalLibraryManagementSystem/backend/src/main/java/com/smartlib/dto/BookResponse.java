package com.smartlib.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String category;
    private Long categoryId;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private Integer quantity;
    private Integer availableQuantity;
    private String imageUrl;
    private boolean available;
}
