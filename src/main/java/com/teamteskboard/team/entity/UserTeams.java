package com.teamteskboard.team.entity;

import com.teamteskboard.task.entity.Task;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id")
    private Task task_id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team_id;

    // 생성자
    public UserTeams(Task task_id, Team team_id) {
        this.task_id = task_id;
        this.team_id = team_id;
    }
}
