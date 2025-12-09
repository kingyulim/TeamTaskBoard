package com.teamteskboard.team.repository;

import com.teamteskboard.team.entity.UserTeams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTeamsRepository extends JpaRepository<UserTeams, Long> {
    List<UserTeams> findAllByTeamId(Long teamId);
    boolean existsByTeamIdAndUserId(Long teamId, Long userId);
}
