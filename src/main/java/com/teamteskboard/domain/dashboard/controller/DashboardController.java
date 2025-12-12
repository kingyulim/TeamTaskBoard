package com.teamteskboard.domain.dashboard.controller;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.dto.ApiResponse;
import com.teamteskboard.domain.dashboard.dto.GetDashboardStatsResponse;
import com.teamteskboard.domain.dashboard.dto.GetMyDashboardResponse;
import com.teamteskboard.domain.dashboard.dto.GetWeeklyDashboardResponse;
import com.teamteskboard.domain.dashboard.service.DashboardService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * @param user 로그인 사용자
     * @return 대시보드 통계
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<GetDashboardStatsResponse>> getDashboardStats(
            @AuthenticationPrincipal SecurityUser user) {

        GetDashboardStatsResponse response = dashboardService.getDashboardStats(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("대시보드 통계 조회 성공", response));
    }

    /**
     * @param user 로그인 사용자
     * @return 대시보드 통계
     */
    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<GetMyDashboardResponse>> getMyDashboard(
            @AuthenticationPrincipal SecurityUser user){

        GetMyDashboardResponse response = dashboardService.getMyDashboard(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("내 작업 요약 조회 성공", response));

    }

    /**
     * @return 주간 작업 추세
     */
    @GetMapping("/weekly-trend")
    public ResponseEntity<ApiResponse<List<GetWeeklyDashboardResponse>>> getWeeklyTrend() {

        List<GetWeeklyDashboardResponse> response = dashboardService.getWeeklyDashboard();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("주간 작업 추세 조회 성공", response));
    }


}
