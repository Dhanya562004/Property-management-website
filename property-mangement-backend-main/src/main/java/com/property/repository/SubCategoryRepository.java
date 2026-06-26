package com.property.repository;

import com.property.model.Category;
import com.property.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    List<SubCategory> findByCategoryAndIsActive(Category category, Integer isActive);
    List<SubCategory> findByIsActive(Integer isActive);
}
