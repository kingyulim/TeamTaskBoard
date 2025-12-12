package com.teamteskboard.domain.comment.repository;

import com.teamteskboard.common.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    //조회에서 사용
    Page<Comment> findByTaskIdAndIsDeletedFalse(Long taskId, Pageable pageable);

    //수정에서 사용
    Optional<Comment> findByIdAndTaskIdAndIsDeletedFalse(Long id, Long taskId);

    //생성 및 삭제에서 사용
    Optional<Comment> findByIdAndIsDeletedFalse(Long parentId);

    //부모댓글
    Page<Comment> findAllByTaskIdAndParentIdIsNullAndIsDeletedFalse(Long taskId, Pageable pageable);

    //대댓글
    List<Comment> findByTaskIdAndParentIdIsNotNullAndIsDeletedFalseOrderByCreatedAtAsc(Long taskId);
}


