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
    @JoinColumn(name = "assignee_id")
    private User assigneeId;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 250, nullable = false)
    private String description;

    @Column(length = 20, nullable = false)
    private String status = "TODO";

    @Column(length = 20, nullable = false)
    private String priority;

    @Column(nullable = false)
    private LocalDateTime dueDatetime;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 생성자
    public Task(String title, String description, String status, String priority, User assigneeId, LocalDateTime dueDatetime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.assigneeId = assigneeId;
    }
}
