package com.teamteskboard.user.repository;

import com.teamteskboard.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
