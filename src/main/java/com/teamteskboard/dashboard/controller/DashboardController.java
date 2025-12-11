package com.teamteskboard.dashboard.controller;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.dashboard.dto.response.GetDashboardStatsResponse;
import com.teamteskboard.dashboard.dto.response.GetMyDashboardResponse;
import com.teamteskboard.dashboard.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<GetDashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal SecurityUser user) {

        GetDashboardStatsResponse response = dashboardService.getDashboardStats(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("대시보드 통계 조회 성공", response));
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<GetMyDashboardResponse>> getMyDashboard(
            @AuthenticationPrincipal SecurityUser user){

        GetMyDashboardResponse response = dashboardService.getMyDashboard(user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("내 작업 요약 조회 성공", response));

    }


}
