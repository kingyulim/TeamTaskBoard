package com.teamteskboard.domain.team.controller;

import com.teamteskboard.common.dto.ApiResponse;
import com.teamteskboard.domain.team.dto.request.*;
import com.teamteskboard.domain.team.dto.response.*;
import com.teamteskboard.domain.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;

    /**
     * 팀 생성
     * @param request 팀 생성 DTO
     * @return 팀 생성 응답 DTO
     */
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> createTeam(
            @Valid @RequestBody CreatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 생성되었습니다.", teamService.createTeam(request)));
    }

    /**
     * 팀 전체 조회
     * @return Team 멤버 및 정보 DTO 리스트
     */
    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<List<GetAllTeamsResponse>>> getTeams() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 목록 조회 성공", teamService.getAllTeams()));
    }

    /**
     * 팀 상세 조회
     * @param id (Team Id)
     * @return Team Id가 포함된 정보 DTO 반환
     */
    @GetMapping("/teams/{id}")
    public ApiResponse<GetOneTeamResponse> getOneTeam(
            @PathVariable Long id
    ) {
        return ApiResponse
                .success("팀 조회 성공", teamService.getOneTeam(id));
    }

    /**
     * 팀 정보 수정
     * @param id (Team Id)
     * @param request 팀 수정 요청 DTO
     * @return 업데이트 된 Team 응답 DTO
     */
    @PutMapping("/teams/{id}")
    public ResponseEntity<ApiResponse<UpdatedTeamResponse>> updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody UpdatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 정보가 수정되었습니다.", teamService.updateTeam(id, request)));
    }

    /**
     * 팀 삭제
     * @param id (Team Id)
     * @return 성공 시 삭제 후 내용 null
     */
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @PathVariable Long id
    ) {
        teamService.deleteTeam(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀이 삭제되었습니다.", null));
    }


    /**
     * 팀 멤버 생성
     * @param teamId (Team Id)
     * @param request 팀 생성 정보 DTO
     * @return 생성된 멤버 응답 DTO
     */
    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> addTeamMember(
            @PathVariable Long teamId,
            @Valid @RequestBody CreatedTeamMemberRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버가 추가되었습니다.", teamService.createdTeamMember(teamId, request))
                );
    }

    /**
     * 팀 멤버 조회
     * @param teamId (Team Id)
     * @return 멤버 정보 List 응답 DTO
     */
    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMember(
            @PathVariable Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버 조회 성공", teamService.getTeamMembers(teamId)));
    }

    /**
     * 팀 멤버 삭제
     * @param teamId (Team Id)
     * @param userId (User Id)
     * @return 성공 시 삭제 후 내용 null
     */
    @DeleteMapping("/teams/{teamId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeTeamMember(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        teamService.removeTeamMember(teamId, userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버가 제거되었습니다.", null));
    }

}
