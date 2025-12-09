package com.teamteskboard.team.repository;

import com.teamteskboard.team.entity.Team;
import com.teamteskboard.team.entity.UserTeams;
import com.teamteskboard.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTeamsRepository extends JpaRepository<UserTeams, Long> {
    List<UserTeams> findAllByTeamId(Long teamId);

    List<UserTeams> findByTeamId(Long teamId);

    boolean existsByTeamAndUser(Team team, User user);

    List<UserTeams> findByTeam(Team team);
}
