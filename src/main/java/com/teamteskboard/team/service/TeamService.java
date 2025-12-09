package com.teamteskboard.team.service;

import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.team.dto.request.CreatedTeamRequest;
import com.teamteskboard.team.dto.response.CreatedTeamResponse;
import com.teamteskboard.team.entity.Team;
import com.teamteskboard.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public CreatedTeamResponse createTeam(CreatedTeamRequest request) {

        // [1] 요청값 검증 — 이름 필수
        if (request.getName() == null || request.getName().isBlank()) {
            throw new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND);
        }

        // [2] 중복 팀 이름 체크
        if (teamRepository.existsByName(request.getName())) {
            throw new CustomException(ExceptionMessageEnum.TEAM_NAME_DUPLICATE);
        }

        // [3] 팀 저장
        Team team = new Team(request.getName(), request.getDescription());
        teamRepository.save(team);

        // [4] 명세상 생성 시 멤버는 비어 있어야 함
        return CreatedTeamResponse.from(team, Collections.emptyList());
    }
}
