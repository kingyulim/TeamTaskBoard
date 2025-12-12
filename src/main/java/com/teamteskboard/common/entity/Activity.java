package com.teamteskboard.common.entity;

import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "activities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 20, nullable = false)
    private ActivityTypeEnum type;

    @Column(length = 250, nullable = false)
    private String description;
    
    // 생성자
    public Activity(Task task, User user, ActivityTypeEnum type, String description) {
        this.task = task;
        this.user = user;
        this.type = type;
        this.description = description;
    }
}
