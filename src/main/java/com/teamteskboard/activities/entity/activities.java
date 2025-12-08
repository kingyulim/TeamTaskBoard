package com.teamteskboard.activities.entity;

import com.teamteskboard.common.entity.BaseTimeEntity;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Activities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class activities extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(length = 20, nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    @Column(length = 250, nullable = false)
    private String description;
    
    // 생성자
    public activities(Task task_id, User user_id, String description) {
        this.task_id = task_id;
        this.user_id = user_id;
        this.description = description;
    }
}
