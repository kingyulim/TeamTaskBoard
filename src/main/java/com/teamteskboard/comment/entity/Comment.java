package com.teamteskboard.comment.entity;

import com.teamteskboard.common.entity.BaseTimeEntity;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user_id;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 생성자
    public  Comment(User user_id, Task task_id, String content, Long parentId) {
        this.user_id = user_id;
        this.task_id = task_id;
        this.content = content;
        this.parentId = parentId;
    }
}
