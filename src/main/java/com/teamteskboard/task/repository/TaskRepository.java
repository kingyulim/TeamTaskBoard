package com.teamteskboard.task.repository;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.enums.TaskStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"assignee"})
    Optional<Task> findById(Long taskId);

    @EntityGraph(attributePaths = {"assignee"})
    @Query("""
        SELECT t FROM Task t
        WHERE t.isDeleted = false
          AND (:status IS NULL OR t.status = :status)
          AND (:assigneeId IS NULL OR t.id = :assigneeId)
          AND (
                :search IS NULL OR
                t.title LIKE %:search% OR
                t.description LIKE %:search%
          )
        """)
    Page<Task> findTasks(
            @Param("status") TaskStatusEnum status,
            @Param("assigneeId") Long assigneeId,
            @Param("search") String search,
            Pageable pageable
    );

}
