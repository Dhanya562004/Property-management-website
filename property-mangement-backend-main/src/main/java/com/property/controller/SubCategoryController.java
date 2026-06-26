package com.property.controller;

import com.property.dto.ApiResponse;
import com.property.model.Category;
import com.property.model.SubCategory;
import com.property.repository.CategoryRepository;
import com.property.repository.SubCategoryRepository;
import com.property.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/subcategory")
public class SubCategoryController {

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/manage/list")
    public ApiResponse listSubCategories(@RequestParam("category_id") Long categoryId) {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (!optionalCategory.isPresent() || optionalCategory.get().getIsActive() != 1) {
                return ApiResponse.success(new ArrayList<>());
            }

            Category category = optionalCategory.get();
            List<SubCategory> subCategories = subCategoryRepository.findByCategoryAndIsActive(category, 1);

            // Structure to match aggregate pipeline in Express
            Map<String, Object> categoryInfo = new HashMap<>();
            categoryInfo.put("_id", category.getId());
            categoryInfo.put("name", category.getName());
            categoryInfo.put("isactive", category.getIsActive());

            List<Map<String, Object>> subList = new ArrayList<>();
            for (SubCategory sc : subCategories) {
                Map<String, Object> scMap = new HashMap<>();
                scMap.put("_id", sc.getId());
                scMap.put("name", sc.getName());
                scMap.put("isactive", sc.getIsActive());
                subList.add(scMap);
            }

            Map<String, Object> groupMap = new HashMap<>();
            groupMap.put("category", categoryInfo);
            groupMap.put("subCategories", subList);

            List<Map<String, Object>> result = new ArrayList<>();
            result.add(groupMap);

            return ApiResponse.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PostMapping("/manage/add")
    public ApiResponse addSubCategory(@RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            String subCatName = body.get("sub_cat_name");
            String categoryIdStr = body.get("category_id");

            if (subCatName == null || subCatName.trim().isEmpty()) {
                return ApiResponse.requiredParam("Subcategory name");
            }
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                return ApiResponse.requiredParam("Category Id");
            }

            Long categoryId;
            try {
                categoryId = Long.parseLong(categoryIdStr);
            } catch (NumberFormatException e) {
                return ApiResponse.invalidInput("Category Id");
            }

            Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
            if (!optionalCategory.isPresent() || optionalCategory.get().getIsActive() != 1) {
                return ApiResponse.notMatch("Category Id");
            }

            SubCategory subCategory = new SubCategory(subCatName, optionalCategory.get());
            subCategoryRepository.save(subCategory);
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @PutMapping("/manage/update/{id}")
    public ApiResponse updateSubCategory(@PathVariable("id") Long id, @RequestBody Map<String, String> body) {
        try {
            UserContext.UserClaims currentUser = UserContext.getClaims();
            if (currentUser == null) {
                return ApiResponse.tokenRequired();
            }

            Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(id);
            if (!optionalSubCategory.isPresent() || optionalSubCategory.get().getIsActive() != 1) {
                return ApiResponse.unknownError();
            }

            SubCategory subCategory = optionalSubCategory.get();
            String subCatName = body.get("sub_cat_name");
            if (subCatName != null && !subCatName.trim().isEmpty()) {
                subCategory.setName(subCatName);
                subCategoryRepository.save(subCategory);
            }

            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }

    @DeleteMapping("/manage/delete/{id}")
    public ApiResponse deleteSubCategory(@PathVariable("id") Long id) {
        try {
            Optional<SubCategory> optionalSubCategory = subCategoryRepository.findById(id);
            if (optionalSubCategory.isPresent()) {
                SubCategory subCategory = optionalSubCategory.get();
                subCategory.setIsActive(0);
                subCategoryRepository.save(subCategory);
            }
            return ApiResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.unknownError();
        }
    }
}
