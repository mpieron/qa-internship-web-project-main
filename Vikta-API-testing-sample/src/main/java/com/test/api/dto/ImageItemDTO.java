package com.test.api.dto;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ImageItemDTO {
    private Long id;
    private String pathToImage;
    private String title;
    private String author;     // this field is missing in documentation, can't be empty
    private int rating;     // this field is missing in documentation, [0,5]
    private String description;
    private List<String> tags;
    private Set<Long> categoryIds;
    private Double price;
}
