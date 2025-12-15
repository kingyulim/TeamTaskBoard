package com.teamteskboard.domain.activity.repository;

import com.teamteskboard.common.entity.Activity;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import com.teamteskboard.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("""
        SELECT a FROM Activity a
        WHERE (:type IS NULL OR a.type = :type)
            AND (:taskId IS NULL OR a.task.id = :taskId)
            AND (:startDate IS NULL OR a.createdAt >= :startDate)
            AND (:endDate IS NULL OR a.createdAt <= :endDate)
        """)
    Page<Activity> findActivities(
            Pageable pageable,
            @Param("type") ActivityTypeEnum type,
            @Param("taskId") Long taskId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    List<Activity> findAllByUser(User user);

    // 특정 날짜에 생성된 작업 개수
    @Query("""
    SELECT COUNT(a)
    FROM Activity a
    WHERE a.type = :type
        AND a.createdAt BETWEEN :start AND :end
        AND a.task.isDeleted = false
    """)
    int countCreatedTasksByDate(
            @Param("type") ActivityTypeEnum type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // 특정 날짜에 완료된 작업 개수
    @Query("""
    SELECT COUNT(a)
    FROM Activity a
    WHERE a.type = :type
        AND a.description LIKE CONCAT('%', :doneKeyword, '%')
        AND a.createdAt BETWEEN :start AND :end
        AND a.task.status = 'DONE'
        AND a.task.isDeleted = false
    """)
    int countCompletedTasksByDate(
            @Param("type") ActivityTypeEnum type,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("doneKeyword") String doneKeyword
    );
}
