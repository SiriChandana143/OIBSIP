package com.smartlib.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    private String title;
    private String author;
    private String isbn;
    private Long categoryId;
    private String publisher;
    private Integer publicationYear;
    private String language;
    private Integer quantity;
    private String imageUrl;
}
