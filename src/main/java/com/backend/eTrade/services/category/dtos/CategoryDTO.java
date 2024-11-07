package com.backend.eTrade.services.category.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<SubcategoryDTO> subcategories;
    private Long parentId;
}
