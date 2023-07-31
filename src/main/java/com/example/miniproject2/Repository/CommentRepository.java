package com.example.miniproject2.Repository;

import com.example.miniproject2.Entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByItemId(Long itemId, Pageable pageable);
}
