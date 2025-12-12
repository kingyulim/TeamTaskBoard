package com.teamteskboard.common.entity;

import jakarta.persistence.*;     // 요 안에 @Id 포함됨
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "teams")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseTimeEntity {
    // 속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String description;

    // 생성자
    public Team(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 기능
    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
