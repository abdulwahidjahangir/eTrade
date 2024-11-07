package com.backend.eTrade.requests.categories;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {
    private Long id = null;
    private String name;
    private String description;
}
