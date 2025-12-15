package com.teamteskboard.common.entity;

import com.teamteskboard.domain.task.dto.request.UpdateTaskRequest;
import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import com.teamteskboard.domain.task.enums.TaskStatusEnum;
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

    @ManyToOne(                     // 이 부분 줄바꿈 유지?
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "assignee_id")
    private User assignee; //assignee

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 250, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TaskStatusEnum status = TaskStatusEnum.TODO;  // 최초 생성 시 기본값

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TaskPriorityEnum priority;

    @Column(nullable = false)
    private LocalDateTime dueDate; // dueDatetime -> dueDate 이름 수정

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 생성자
    public Task(String title, String description, TaskPriorityEnum priority, User assignee, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.assignee = assignee;
        this.dueDate = dueDate;
    }

    public void update(UpdateTaskRequest request, User updatedAssignee) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.priority = request.getPriority();
        this.assignee = updatedAssignee;
        this.dueDate = (request.getDueDate() != null ? request.getDueDate() : LocalDateTime.now().plusDays(7));

    }

    public void updateStatus(TaskStatusEnum status) {
        this.status = status;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
