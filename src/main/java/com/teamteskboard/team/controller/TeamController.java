package com.teamteskboard.team.controller;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.team.dto.request.CreatedTeamMemberRequest;
import com.teamteskboard.team.dto.request.CreatedTeamRequest;
import com.teamteskboard.team.dto.request.UpdatedTeamRequest;
import com.teamteskboard.team.dto.response.*;
import com.teamteskboard.team.service.TeamService;
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
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> createTeamApi(
            @RequestBody CreatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 생성되었습니다.", teamService.createTeam(request)));
    }

    // 팀 목록 전체 조회 API
    @GetMapping("/teams")
    public ResponseEntity<ApiResponse<List<GetAllTeamsResponse>>> getTeamsApi() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 목록 조회 성공", teamService.getAllTeams()));
    }

    // 팀 상세 조회 API
    @GetMapping("/teams/{id}")
    public ApiResponse<GetOneTeamResponse> getOneTeamApi(
            @PathVariable Long id
    ) {
        return ApiResponse.success("팀 조회 성공", teamService.getOneTeam(id));
    }

    // 팀 수정 API
    @PutMapping("/teams/{Id}")
    public ResponseEntity<ApiResponse<UpdatedTeamResponse>> updateTeamApi(
            @PathVariable Long Id,
            @RequestBody UpdatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 정보가 수정되었습니다.", teamService.updateTeam(Id, request)));
    }

    // 팀 멤버 추가 API
    @PostMapping("/teams/{teamId}/members")
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> addTeamMemberApi(
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
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMemberApi(
            @PathVariable Long teamId
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버 조회 성공", teamService.getTeamMembers(teamId)));
    }

    // 팀 멤버 제거 API
    @DeleteMapping("/teams/{teamId}/members/{userId}")
    public ResponseEntity<ApiResponse<Void>> removeTeamMemberApi(
            @PathVariable Long teamId,
            @PathVariable Long userId
    ) {
        teamService.removeTeamMember(teamId, userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 멤버가 제거되었습니다.", null));
    }


}
