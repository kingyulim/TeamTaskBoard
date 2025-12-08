package com.teamteskboard.task.entity;

import com.teamteskboard.common.entity.BaseTimeEntity;
import com.teamteskboard.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "user_id")
    private User userId;

    @Column(
            length = 20,
            nullable = false
    )
    private String title;

    @Column(
            length = 250,
            nullable = false
    )
    private String description;

    @Column(
            length = 20,
            nullable = false
    )
    private String status;

    @Column(
            length = 20,
            nullable = false
    )
    private String priority;

    @Column(nullable = false)
    private Long assigneeId;

    @Column(nullable = false)
    private LocalDateTime dueDatetime;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 생성자
    public Task(User userId, String title, String description, String status, String priority, Long assigneeId, LocalDateTime dueDatetime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assigneeId = assigneeId;
        this.userId = userId;
    }
}
