package com.teamteskboard.comment.entity;

import com.teamteskboard.common.entity.BaseTimeEntity;
import com.teamteskboard.task.entity.Task;
import com.teamteskboard.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "user_id")
    private User user;

    @Column(
            columnDefinition = "text",
            nullable = false
    )
    private String content;

    @Column(nullable = true)
    private Long parentId;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 생성자
    public  Comment(User user, Task task, String content, Long parentId) {
        this.user = user;
        this.task = task;
        this.content = content;
        this.parentId = parentId;
    }

    //기능
    public void commentUpdate(String content) {
        this.content = content;
    }

    public void getIsDelete(){
        this.isDeleted = true;
    }


}
