package com.teamteskboard.domain.user.repository;

import com.teamteskboard.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    // 소프트딜리트 되지 않은 사용자 중에서 조회
    Optional<User> findByIdAndIsDeletedFalse(Long id);

    // 내 이메일 말고 다른 사람 중에 이 이메일 쓰는 사람 있나 체크
    Boolean existsByEmailAndIdNot(String email, Long id);

    Boolean existsByUserName(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:keyword% OR u.userName LIKE %:keyword%")
    List<User> findByKeyword(@Param("keyword") String keyword);

    List<User> findAllByIsDeletedFalse();
}
