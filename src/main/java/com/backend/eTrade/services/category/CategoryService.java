package com.backend.eTrade.services.category;

import com.backend.eTrade.models.categories.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    List<Category> getSubCategories(Long parentId);

    Category createCategory(String name, String description, Long parentId);

    Category updateCategory(Long id, String name, String description);

    Category moveCategory(Long categoryId, Long newParentId);

    Category removeParent(Long categoryId);

    void deleteCategory(Long id);
}