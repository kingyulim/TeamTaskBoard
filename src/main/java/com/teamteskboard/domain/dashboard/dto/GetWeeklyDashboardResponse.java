package com.teamteskboard.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetWeeklyDashboardResponse {

    private final String name; //요일
    private final int tasks; //생성된 작업 수
    private final int completed; // 완료된 작업 수
    private final String date; // 날짜(연-월-일)

    public static GetWeeklyDashboardResponse from (String name, int tasks, int completed, String date) {
        return new GetWeeklyDashboardResponse(
                name,
                tasks,
                completed,
                date);
    }

}
