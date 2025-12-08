package com.teamteskboard.team.entity;

import com.teamteskboard.task.entity.Task;
import com.teamteskboard.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "usersteams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeams {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "task_id")
    private Task task_id;

    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "user_id")
    private User user_id;

    // 생성자
    public UserTeams(Task task_id, User user_id) {
        this.task_id = task_id;
        this.user_id = user_id;
    }
}
