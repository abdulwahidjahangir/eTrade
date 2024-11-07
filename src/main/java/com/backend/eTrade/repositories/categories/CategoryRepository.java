package com.backend.eTrade.repositories.categories;

import com.backend.eTrade.models.categories.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Category c WHERE c.name = ?1 AND c.parentCategory.id = ?2")
    boolean existsByNameAndParentId(String name, Long parentId);

    List<Category> findByParentCategory_Id(Long parentId);

    boolean existsByName(String name);
}