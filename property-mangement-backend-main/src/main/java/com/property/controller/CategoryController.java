package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Category;
import com.property.repository.CategoryRepository;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/manage/list")
    public ApiResponse listCategories() {
        try {
            List<Category> categories = categoryRepository.findByIsActive(1);
            return ApiResponse.success(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PostMapping("/manage/add")
    public ApiResponse addCategory(@RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            String catName = body.get("cat_name");
            if (catName == null || catName.trim().isEmpty()) {
                return ApiResponse.requiredParam("Category name");
            }

            Category category = new Category(catName);
            categoryRepository.save(category);
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/manage/update/{id}")
    public ApiResponse updateCategory(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (!optionalCategory.isPresent() || optionalCategory.get().getIsActive() != 1) {
                return ApiResponse.unknownError();
            }

            Category category = optionalCategory.get();
            String catName = body.get("cat_name");
            if (catName != null && !catName.trim().isEmpty()) {
                category.setName(catName);
                categoryRepository.save(category);
            }

            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @DeleteMapping("/manage/delete/{id}")
    public ApiResponse deleteCategory(@PathVariable("id") Long id) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                category.setIsActive(0);
                categoryRepository.save(category);
            }
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
