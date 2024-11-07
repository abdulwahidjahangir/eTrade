package com.backend.eTrade.services.category.impl;

import com.backend.eTrade.error.category.CategoryException;
import com.backend.eTrade.models.categories.Category;
import com.backend.eTrade.repositories.categories.CategoryRepository;
import com.backend.eTrade.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        Map<Long, Category> categoryMap = new HashMap<>();
        for (Category category : categories) {
            categoryMap.put(category.getId(), category);
        }

        List<Category> topLevelCategories = new ArrayList<>();

        for (Category category : categories) {
            if (category.getParentCategory() == null) {
                topLevelCategories.add(category);
            } else {
                Category parent = category.getParentCategory();
                parent.getSubcategories().add(category);
            }
        }

        return topLevelCategories;
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("Category not found", HttpStatus.NOT_FOUND));

        return category;
    }

    @Override
    public List<Category> getSubCategories(Long parentId) {
        List<Category> categories = categoryRepository.findByParentCategory_Id(parentId);

        return categories;
    }

    @Override
    public Category createCategory(String name, String description, Long parentId) {
        name = name.trim();
        name = name.toLowerCase();

        if (name.isEmpty()) {
            throw new CategoryException("Invalid Category Name", HttpStatus.BAD_REQUEST);
        }

        if (parentId != null && categoryRepository.existsByNameAndParentId(name, parentId)) {
            throw new CategoryException("A category with the same name and parent ID already exists.", HttpStatus.CONFLICT);
        }

        if (parentId == null && categoryRepository.existsByName(name)) {
            throw new CategoryException("A category with the same name already exists.", HttpStatus.CONFLICT);
        }

        Category newCategory = new Category();
        newCategory.setName(name);
        newCategory.setDescription(description);

        if (parentId != null) {
            Category category = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new CategoryException("Invalid Parent Id", HttpStatus.BAD_REQUEST));
            newCategory.setParentCategory(category);
        }

        return categoryRepository.save(newCategory);
    }


    @Override
    public Category updateCategory(Long id, String name, String description) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryException("No CategoryFound", HttpStatus.BAD_REQUEST));

        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    @Override
    public Category moveCategory(Long categoryId, Long newParentId) {

        Category categoryToMove = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category Not Found", HttpStatus.BAD_REQUEST));

        Category newParentCategory = categoryRepository.findById(newParentId)
                .orElseThrow(() -> new CategoryException("Parent Category Not Found", HttpStatus.BAD_REQUEST));

        if (Objects.equals(categoryToMove.getId(), newParentCategory.getId())) {
            throw new CategoryException("A category can not be it's parent category", HttpStatus.BAD_REQUEST);
        }

        categoryToMove.setParentCategory(newParentCategory);

        return categoryRepository.save(categoryToMove);
    }

    @Override
    public Category removeParent(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found", HttpStatus.BAD_REQUEST));

        category.setParentCategory(null);

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryException("Category not found", HttpStatus.BAD_REQUEST);
        }
        categoryRepository.deleteById(id);
    }
}
