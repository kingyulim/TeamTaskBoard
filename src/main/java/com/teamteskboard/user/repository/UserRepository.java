package com.teamteskboard.user.repository;

import com.teamteskboard.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserName(String username);

    Optional<User> findByEmail(String email);

    // 내 이메일 말고 다른 사람 중에 이 이메일 쓰는 사람 있나 체크
    Boolean existsByEmailAndIdNot(String email, Long id);

    Boolean existsByUserName(String email);

    Boolean existsByEmail(String email);
}
