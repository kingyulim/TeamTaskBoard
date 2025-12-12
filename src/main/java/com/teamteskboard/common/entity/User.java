package com.teamteskboard.common.entity;

import com.teamteskboard.domain.user.enums.UserRoleEnum;
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

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String userName;

    @Column(length = 20, nullable = false)
    private String email;

    @Column(length = 250, nullable = false)
    private String password;

    @Column(length = 20, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // 생성자
    public User(String name, String userName, String email, String password, UserRoleEnum role) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // 기능

    /**
     * 회원 삭제 기능 메서드
     * @param isDelete 회원 탈퇴 삭제 매개변수
     */
    public void userDelete(Boolean isDelete) {
        this.isDeleted = isDelete;
    }

    /**
     * 회원 업데이트 기능 메서드
     * @param name 회원 이름
     * @param email 회원 이메일
     */
    public void userUpdate(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
