package com.backend.eTrade.controllers.categories;

import com.backend.eTrade.models.categories.Category;
import com.backend.eTrade.requests.categories.CategoryRequest;
import com.backend.eTrade.services.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/get-all-categories")
    public ResponseEntity<List<Category>> getALlCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/public/get-category/{id}")
    public ResponseEntity<Category> getCategory(
            @PathVariable("id") Long categoryId
    ) {
        Category category = categoryService.getCategoryById(categoryId);
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/public/get-subcategories/{id}")
    public ResponseEntity<List<Category>> getSubCategories(
            @PathVariable("id") Long categoryId
    ) {
        List<Category> category = categoryService.getSubCategories(categoryId);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping("/admin/create-category")
    public ResponseEntity<?> createCategory(
            @RequestBody CategoryRequest categoryRequest
    ) {
        System.out.println(categoryRequest.getId());
        Category category = categoryService.createCategory(categoryRequest.getName(), categoryRequest.getDescription(), categoryRequest.getId());
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/admin/update-category")
    public ResponseEntity<?> updateCategory(
            @RequestBody CategoryRequest categoryRequest
    ) {
        Category category = categoryService.updateCategory(categoryRequest.getId(), categoryRequest.getName(), categoryRequest.getDescription());
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/admin/move-category")
    public ResponseEntity<?> moveCategory(
            @RequestParam Long categoryId,
            @RequestParam Long parentId
    ) {
        Category category = categoryService.moveCategory(categoryId, parentId);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/admin/remove-parent")
    public ResponseEntity<?> removeParent(
            @RequestParam Long categoryId
    ) {
        Category category = categoryService.removeParent(categoryId);
        return ResponseEntity.ok().body(category);
    }

    @PutMapping("/admin/add-category")
    public ResponseEntity<?> addCategory(
            @RequestParam Long categoryId,
            @RequestParam Long parentId
    ) {
        Category category = categoryService.moveCategory(categoryId, parentId);
        return ResponseEntity.ok().body(category);
    }

    @DeleteMapping("/admin/delete-category/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable("id") Long categoryId
    ) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().body("Category Deleted Successfully");
    }
}
