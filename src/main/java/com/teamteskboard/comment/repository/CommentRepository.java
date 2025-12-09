package com.teamteskboard.comment.repository;

import com.teamteskboard.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByIsDeleted(Boolean isDeleted);
}
