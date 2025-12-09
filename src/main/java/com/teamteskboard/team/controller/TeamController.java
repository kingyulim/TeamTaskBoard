package com.teamteskboard.team.controller;

import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.team.dto.request.CreatedTeamRequest;
import com.teamteskboard.team.dto.response.CreatedTeamResponse;
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

}
