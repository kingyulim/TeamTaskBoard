package com.teamteskboard.team.controller;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.team.dto.request.CreatedTeamRequest;
import com.teamteskboard.team.dto.request.UpdatedTeamRequest;
import com.teamteskboard.team.dto.response.CreatedTeamResponse;
import com.teamteskboard.team.dto.response.UpdatedTeamResponse;
import com.teamteskboard.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TeamController {

    private final TeamService teamService;

    @PostMapping("/teams")
    public ResponseEntity<ApiResponse<CreatedTeamResponse>> createTeamApi(
            @RequestBody CreatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("팀이 생성되었습니다.", teamService.createTeam(request)));
    }

    @PutMapping("/teams/{teamId}")
    public ResponseEntity<ApiResponse<UpdatedTeamResponse>> updateTeamApi(
            @PathVariable Long teamId,
            @RequestBody UpdatedTeamRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("팀 정보가 수정되었습니다.", teamService.updateTeam(teamId, request)));
    }

}
