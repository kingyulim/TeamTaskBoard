package com.teamteskboard.team.entity;

import com.teamteskboard.common.entity.BaseTimeEntity;
import com.teamteskboard.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "usersteams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTeams extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id")
    private Team team;

    // 생성자
    public UserTeams(Team team, User user) {
        this.team = team;
        this.user = user;
    }
}
