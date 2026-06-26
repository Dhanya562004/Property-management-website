package com.property.repository;

import com.property.model.Category;
import com.property.model.Property;
import com.property.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByIsActiveOrderByPostedOnDesc(Integer isActive);

    List<Property> findByIsActiveAndPropStatusOrderByPostedOnDesc(Integer isActive, Integer propStatus);

    @Query(value = "SELECT * FROM properties WHERE isactive = 1 ORDER BY RAND() LIMIT 8", nativeQuery = true)
    List<Property> findRandomProperties();

    List<Property> findByIsActiveAndListedbyIdOrderByPostedOnDesc(Integer isActive, Long listedbyId);

    List<Property> findByIsActiveAndCategoryAndSubcategoryOrderByPostedOnDesc(Integer isActive, Category category, SubCategory subcategory);

    List<Property> findByIsActiveAndCategoryOrderByPostedOnDesc(Integer isActive, Category category);

    long countByIsActive(Integer isActive);

    @Query("SELECT p FROM Property p WHERE p.isActive = 1 " +
           "AND (:propertyName IS NULL OR LOWER(p.propertyName) LIKE LOWER(CONCAT('%', :propertyName, '%'))) " +
           "AND (:city IS NULL OR LOWER(p.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:locality IS NULL OR LOWER(p.locality) LIKE LOWER(CONCAT('%', :locality, '%')))")
    List<Property> searchProperties(@Param("propertyName") String propertyName,
                                    @Param("city") String city,
                                    @Param("locality") String locality);
}
