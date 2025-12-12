package com.teamteskboard.domain.team.repository;

import com.teamteskboard.common.entity.Team;
import com.teamteskboard.common.entity.UserTeams;
import com.teamteskboard.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserTeamsRepository extends JpaRepository<UserTeams, Long> {
    List<UserTeams> findAllByTeamId(Long teamId);

    List<UserTeams> findByTeamId(Long teamId);

    boolean existsByTeamAndUser(Team team, User user);

    List<UserTeams> findByTeam(Team team);

    List<UserTeams> findAllByTeam(Team team);

    Optional<UserTeams> findByTeamAndUser(Team team, User user);
}
