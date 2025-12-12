package com.teamteskboard.domain.team.repository;

import com.teamteskboard.common.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    boolean existsByName(String name);

    @Query("SELECT te FROM Team te WHERE te.name LIKE %:keyword% OR te.description LIKE %:keyword%")
    List<Team> findByKeyword(@Param("keyword") String keyword);
}
