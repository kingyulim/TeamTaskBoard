package com.teamteskboard.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetDashboardStatsResponse {

    private final int totalTasks;   //전체 작업
    private final int completedTasks;// 완료 작업(done)
    private final int inProgressTasks;// 진행중(IN_PROGRESS)
    private final int todoTasks;//(TODO)

    private final int overdueTasks;// 기한 초과

    private final double teamProgress; //팀 진행률
    private final double completionRate;//내 진행률


}