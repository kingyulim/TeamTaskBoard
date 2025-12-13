package com.teamteskboard.domain.team.service;

import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.domain.team.dto.request.*;
import com.teamteskboard.domain.team.dto.response.*;
import com.teamteskboard.common.entity.Team;
import com.teamteskboard.common.entity.UserTeams;
import com.teamteskboard.domain.team.repository.TeamRepository;
import com.teamteskboard.domain.team.repository.UserTeamsRepository;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamsRepository userTeamsRepository;
    private final UserRepository userRepository;

    /**
     * 팀 생성
     * @param request 팀 생성 입력 값 파라미터
     * @return ApiResponse<CreatedTeamResponse> 반환
     */
    @Transactional
    public CreatedTeamResponse createTeam(CreatedTeamRequest request) {
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

    /**
     * 팀 전체 조회
     * @return List<GetAllTeamsResponse> 반환
     */
    @Transactional(readOnly = true)
    public List<GetAllTeamsResponse> getAllTeams() {
        // 모든 팀 조회
        List<Team> teams = teamRepository.findAll();

        // 각 팀별 멤버 조회 및 매핑
        return teams.stream()
                .map(team -> {
                    List<UserTeams> members = userTeamsRepository.findAllByTeamId(team.getId());

                    List<TeamMemberResponse> memberResponses = members.stream()
                            .map(ut -> new TeamMemberResponse(
                                    ut.getUser().getId(),
                                    ut.getUser().getUserName(),
                                    ut.getUser().getName(),
                                    ut.getUser().getEmail(),
                                    ut.getUser().getRole().name(),
                                    ut.getCreatedAt()
                            ))
                            .toList();

                    return GetAllTeamsResponse.from(team, memberResponses);
                })
                .toList();
    }

    /**
     * 팀 상세 조회
     * @param id (Team Id)
     * @return 멤버 정보 리스트가 포함된 GetOneTeamResponse 반환
     */
    @Transactional(readOnly = true)
    public GetOneTeamResponse getOneTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        List<UserTeams> userTeams = userTeamsRepository.findByTeamId(team.getId());

        List<TeamMemberResponse> members = userTeams.stream()
                .map(ut -> new TeamMemberResponse(
                        ut.getUser().getId(),
                        ut.getUser().getUserName(),
                        ut.getUser().getName(),
                        ut.getUser().getEmail(),
                        ut.getUser().getRole().name(),
                        ut.getUser().getCreatedAt()
                ))
                .toList();


        return GetOneTeamResponse.from(team, members);
    }


    /**
     * 팀 정보 수정
     * @param teamId (Team Id)
     * @param request 수정할 정보 요청 DTO
     * @param loginUserId 권한 검증에 필요한 UserId
     * @return 수정된 정보 응답 DTO
     */
    @Transactional
    public UpdatedTeamResponse updateTeam(Long teamId, UpdatedTeamRequest request, Long loginUserId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        // 권한 검증 로직(멤버에 포함된 유저만 수정 가능)
        boolean memberCheck = userTeamsRepository.existsByTeamIdAndUserId(teamId, loginUserId);
        if (!memberCheck) {
            throw new CustomException(ExceptionMessageEnum.FORBIDDEN_ACTION);
        }

        // 필드 업데이트
        team.update(request.getName(), request.getDescription());
        teamRepository.save(team);

        // 멤버 전체 조회
        List<UserTeams> members = userTeamsRepository.findAllByTeamId(teamId);

        List<TeamMemberResponse> memberResponses = members.stream()
                .map(ut -> new TeamMemberResponse(
                        ut.getUser().getId(),
                        ut.getUser().getUserName(),
                        ut.getUser().getName(),
                        ut.getUser().getEmail(),
                        ut.getUser().getRole().name(),
                        ut.getCreatedAt()
                ))
                .toList();

        return UpdatedTeamResponse.from(team, memberResponses);
    }

    /**
     * 팀 삭제
     * @param teamId (Team Id)
     */
    @Transactional
    public void deleteTeam(Long teamId) {

        // 팀 존재 여부 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        // 팀에 멤버 존재 여부 확인
        List<UserTeams> members = userTeamsRepository.findAllByTeam(team);
        if (!members.isEmpty()) {
            throw new CustomException(ExceptionMessageEnum.TEAM_DELETE_HAS_MEMBERS);
        }

        teamRepository.delete(team);
    }

    /**
     * 팀 멤버 추가
     * @param teamId (Team Id)
     * @param request 추가할 멤버 요청 DTO
     * @return 팀 + 추가된 멤버 응답 DTO
     */
    @Transactional
    public CreatedTeamResponse createdTeamMember(Long teamId, CreatedTeamMemberRequest request) {

        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        // 유저 존재 확인
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        // 이미 가입된 멤버인지 확인
        boolean exists = userTeamsRepository.existsByTeamAndUser(team, user);
        if (exists) {
            throw new CustomException(ExceptionMessageEnum.TEAM_MEMBER_ALREADY_EXISTS);
        }

        // 중간 테이블 저장 (팀-유저 매핑)
        UserTeams userTeams = new UserTeams(team, user);
        userTeamsRepository.save(userTeams);

        // 현재 팀 멤버 전체 조회
        List<UserTeams> teamUsers = userTeamsRepository.findByTeam(team);

        List<TeamMemberResponse> members = teamUsers.stream()
                .map(ut -> new TeamMemberResponse(
                        ut.getUser().getId(),
                        ut.getUser().getUserName(),
                        ut.getUser().getName(),
                        ut.getUser().getEmail(),
                        ut.getUser().getRole().name(),
                        ut.getUser().getCreatedAt()
                ))
                .toList();

        return CreatedTeamResponse.from(team, members);
    }

    /**
     * 팀 멤버 조회
     * @param teamId (Team Id)
     * @return List<TeamMemberResponse> 반환
     */
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getTeamMembers(Long teamId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        List<UserTeams> userTeams = userTeamsRepository.findAllByTeam(team);

        return userTeams.stream()
                .map(ut -> {
                    User user = ut.getUser();
                    return new TeamMemberResponse(
                            user.getId(),
                            user.getUserName(),
                            user.getName(),
                            user.getEmail(),
                            user.getRole().name(),
                            user.getCreatedAt()
                    );
                })
                .toList();
    }

    /**
     * 팀 멤버 삭제
     * @param teamId (Team Id)
     * @param userId (User Id)
     * @param loginUserId 권한 검증을 위한 로그인 유저 ID값
     */
    @Transactional
    public void removeTeamMember(Long teamId, Long userId, Long loginUserId) {

        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_NOT_FOUND));

        // 유저 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        // 제거 권한 검증(멤버에 포함된 유저만 삭제 가능)
        boolean memberCheck = userTeamsRepository.existsByTeamIdAndUserId(teamId, loginUserId);
        if (!memberCheck) {
            throw new CustomException(ExceptionMessageEnum.FORBIDDEN_ACTION);
        }

        // 팀 멤버 존재 확인
        UserTeams userTeam = userTeamsRepository.findByTeamAndUser(team, user)
                .orElseThrow(() -> new CustomException(ExceptionMessageEnum.TEAM_MEMBER_NOT_FOUND));

        userTeamsRepository.delete(userTeam);
    }
}