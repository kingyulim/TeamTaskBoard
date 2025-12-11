package com.teamteskboard.task.repository;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.task.enums.TaskStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"assignee"})
    Optional<Task> findById(Long taskId);

    @EntityGraph(attributePaths = {"assignee"})
    @Query("""
        SELECT t FROM Task t
        WHERE t.isDeleted = false
          AND (:status IS NULL OR t.status = :status)
          AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)
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

    Optional<Task> findByIdAndIsDeletedFalse(Long taskId);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.isDeleted = false")
    int countAll();

    @Query("select count(t) from Task t where t.isDeleted = false and t.status = 'DONE'")
    int countCompleted();

    @Query("select count(t) from Task t where t.isDeleted = false and t.status = 'IN_PROGRESS'")
    int countInProgress();

    @Query("select count(t) from Task t where t.isDeleted = false and t.status = 'TODO'")
    int countTodo();

    @Query("""
       select count(t)
       from Task t
       where t.isDeleted = false
         and t.status <> 'DONE'
         and t.dueDate < CURRENT_TIMESTAMP
       """)
    int countOverdue();     // 완료되지 않은 작업 중에서 기한이 지난 작업

    @Query("select count(t) from Task t where t.isDeleted = false and t.assignee.id = :userId")
    int countAllByUser(@Param("userId") Long userId);

    @Query("select count(t) from Task t where t.isDeleted = false and t.assignee.id = :userId and t.status = 'DONE'")
    int countCompletedByUser(@Param("userId") Long userId);

    // 오늘 마감되는 작업
    @Query("""
    SELECT t FROM Task t
    WHERE t.isDeleted = false
      AND t.assignee.id = :userId
      AND DATE(t.dueDate) = CURRENT_DATE
    """)
    List<Task> findTodayTasks(@Param("userId") Long userId);


    // 다가오는 작업 (오늘 이후)
    @Query("""
    SELECT t FROM Task t
    WHERE t.isDeleted = false
      AND t.assignee.id = :userId
      AND t.dueDate > CURRENT_DATE
    """)
    List<Task> findUpcomingTasks(@Param("userId") Long userId);


    // 마감일 지난 작업 (완료되지 않은 작업)
    @Query("""
    SELECT t FROM Task t
    WHERE t.isDeleted = false
      AND t.assignee.id = :userId
      AND t.status <> 'DONE'
      AND t.dueDate < CURRENT_TIMESTAMP
    """)
    List<Task> findOverdueTasks(@Param("userId") Long userId);


}
