package com.teamteskboard.domain.team.controller;

import com.teamteskboard.common.dto.ApiResponse;
import com.teamteskboard.domain.team.dto.request.*;
import com.teamteskboard.domain.team.dto.response.*;
import com.teamteskboard.domain.team.service.TeamService;
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

    // 팀 생성 API
    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> createTeam(
            @RequestBody CreatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 생성되었습니다.", teamService.createTeam(request)));
    }

    // 팀 목록 전체 조회 API
    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<List<GetAllTeamsResponse>>> getTeams() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 목록 조회 성공", teamService.getAllTeams()));
    }

    // 팀 상세 조회 API
    @GetMapping("/teams/{id}")
    public ApiResponse<GetOneTeamResponse> getOneTeam(
            @PathVariable Long id
    ) {
        return ApiResponse
                .success("팀 조회 성공", teamService.getOneTeam(id));
    }

    // 팀 수정 API
    @PutMapping("/teams/{id}")
    public ResponseEntity<ApiResponse<UpdatedTeamResponse>> updateTeam(
            @PathVariable Long id,
            @RequestBody UpdatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 정보가 수정되었습니다.", teamService.updateTeam(id, request)));
    }

    // 팀 삭제 API
    @DeleteMapping("/teams/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTeam(
            @PathVariable Long id
    ) {
        teamService.deleteTeam(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀이 삭제되었습니다.", null));
    }


    // 팀 멤버 추가 API
    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> addTeamMember(
            @PathVariable Long teamId,
            @RequestBody CreatedTeamMemberRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버가 추가되었습니다.", teamService.createdTeamMember(teamId, request))
        );
    }

    // 팀 멤버 조회 API
    @GetMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMember(
            @PathVariable Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버 조회 성공", teamService.getTeamMembers(teamId)));
    }

    // 팀 멤버 제거 API
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
