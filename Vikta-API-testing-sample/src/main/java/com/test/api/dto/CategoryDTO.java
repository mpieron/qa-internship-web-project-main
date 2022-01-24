package com.test.api.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CategoryDTO {
    private long id;
    private String pathToCatImage;
    private String title;
    private String description;
    private Set<Long> imageItemIds = new HashSet<>();
}
