package com.property.repository;

import com.property.model.Comment;
import com.property.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPropertyAndIsActiveOrderByPostedOnDesc(Property property, Integer isActive);
}
