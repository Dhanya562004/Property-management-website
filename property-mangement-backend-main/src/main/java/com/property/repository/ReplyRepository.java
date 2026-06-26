package com.property.repository;

import com.property.model.Comment;
import com.property.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByCommentAndIsActiveOrderByPostedOnDesc(Comment comment, Integer isActive);
}
