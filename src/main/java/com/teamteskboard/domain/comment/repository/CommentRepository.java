package com.teamteskboard.domain.comment.repository;

import com.teamteskboard.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByTaskIdAndIsDeletedFalse(Long taskId, Pageable pageable);

    Optional<Comment> findByIdAndTaskIdAndIsDeletedFalse(Long id, Long taskId);

    Optional<Comment> findByIdAndIsDeletedFalse(Long parentId);
}
