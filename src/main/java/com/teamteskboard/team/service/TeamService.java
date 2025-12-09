package com.teamteskboard.team.service;

import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.team.dto.request.CreatedTeamRequest;
import com.teamteskboard.team.dto.request.UpdatedTeamRequest;
import com.teamteskboard.team.dto.response.CreatedTeamResponse;
import com.teamteskboard.team.dto.response.GetAllTeamsResponse;
import com.teamteskboard.team.dto.response.TeamMemberResponse;
import com.teamteskboard.team.dto.response.UpdatedTeamResponse;
import com.teamteskboard.team.entity.Team;
import com.teamteskboard.team.entity.UserTeams;
import com.teamteskboard.team.repository.TeamRepository;
import com.teamteskboard.team.repository.UserTeamsRepository;
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
    private final UserTeamsRepository userTeamsRepository;

    // 팀 생성
    @Transactional
    public CreatedTeamResponse createTeam(CreatedTeamRequest request) {

        // 요청값 검증 — 이름 필수 (Valid로 뺄 예정)
        if (request.getName() == null || request.getName().isBlank()) {
            throw new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND);
        }

        // 중복 팀 이름 체크
        if (teamRepository.existsByName(request.getName())) {
            throw new CustomException(ExceptionMessageEnum.TEAM_NAME_DUPLICATE);
        }

        // 팀 저장
        Team team = new Team(request.getName(), request.getDescription());
        teamRepository.save(team);

        // 명세상 생성 시 멤버는 빈 공란
        return CreatedTeamResponse.from(team, Collections.emptyList());
    }

    // 팀 전체 조회


    // 팀 수정
    public UpdatedTeamResponse updateTeam(Long teamId, UpdatedTeamRequest request) {

        // [임시]
        Long currentUserId = 1L;

        // 1. 팀 존재 여부 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

//        // 2. 수정 권한 체크: 팀 멤버인지 확인
//        boolean isMember = userTeamsRepository.existsByTeamIdAndUserId(teamId, currentUserId);
//        if (!isMember) {
//            throw new CustomException(ExceptionMessageEnum.FORBIDDEN_ACTION);
//        }

        // 3. 필드 업데이트
        team.update(request.getName(), request.getDescription());
        teamRepository.save(team);

        // 4. 멤버 전체 조회
        List<UserTeams> members = userTeamsRepository.findAllByTeamId(teamId);

        List<TeamMemberResponse> memberResponses = members.stream()
                .map(ut -> new TeamMemberResponse(
                        ut.getUser().getId(),
                        ut.getUser().getUsername(),
                        ut.getUser().getName(),
                        ut.getUser().getEmail(),
                        ut.getUser().getRole()
                ))
                .toList();

        // 5. 수정된 팀 정보 반환
        return UpdatedTeamResponse.from(team, memberResponses);
    }

    // 팀 목록 전체 조회
    @Transactional(readOnly = true)
    public List<GetAllTeamsResponse> getAllTeams() {

        // 1. 모든 팀 조회
        List<Team> teams = teamRepository.findAll();

        // 2. 각 팀별 멤버 조회 및 매핑
        return teams.stream()
                .map(team -> {
                    List<UserTeams> members = userTeamsRepository.findAllByTeamId(team.getId());

                    List<TeamMemberResponse> memberResponses = members.stream()
                            .map(ut -> new TeamMemberResponse(
                                    ut.getUser().getId(),
                                    ut.getUser().getUsername(),
                                    ut.getUser().getName(),
                                    ut.getUser().getEmail(),
                                    ut.getUser().getRole()
                            ))
                            .toList();

                    return GetAllTeamsResponse.from(team, memberResponses);
                })
                .toList();
    }
}
