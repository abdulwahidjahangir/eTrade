package com.backend.eTrade.services.category.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SubcategoryDTO {
    private Long id;
    private String name;
    private String description;
}
