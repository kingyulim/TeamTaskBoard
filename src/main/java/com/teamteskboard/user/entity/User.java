package com.teamteskboard.user.entity;

import com.teamteskboard.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    //속성
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            length = 20,
            nullable = false
    )
    private String username;

    @Column(
            length = 20,
            nullable = false
    )
    private String email;

    @Column(
            length = 250,
            nullable = false
    )
    private String password;

    @Column(
            length = 20,
            nullable = false
    )
    private String role;

    @Column(nullable = false)
    private Boolean disDeleted = false;

    // 생성자
    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // 기능
}
