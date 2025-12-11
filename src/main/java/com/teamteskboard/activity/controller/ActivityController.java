package com.teamteskboard.activity.controller;

import com.teamteskboard.activity.dto.response.ReadActivityMeResponse;
import com.teamteskboard.activity.dto.response.ReadActivityResponse;
import com.teamteskboard.activity.service.ActivityService;
import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.dto.response.ApiResponse;
import com.teamteskboard.common.enums.ActivityTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActivityController {

    private final ActivityService activityService;

    /**
     * 전체 활동 로그 조회
     * @param page 보려는 페이지
     * @param size 한번에 보려는 로그 수
     * @param type 활동 타입
     * @param taskId 작업 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 전체 활동 로그 (페이징)
     */
    @GetMapping("/activities")
    public ResponseEntity<ApiResponse<Page<ReadActivityResponse>>> getActivities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ActivityTypeEnum type,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<ReadActivityResponse> result = activityService.getActivities(type, taskId, startDate, endDate, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("활동 로그 조회 성공", result));
    }

    /**
     * 내 활동 로그 조회
     * @param user 로그인한 사용자
     * @return 내 활동 로그
     */
    @GetMapping("/activities/me")
    public ResponseEntity<ApiResponse<List<ReadActivityMeResponse>>> getActivitiesMe(
            @AuthenticationPrincipal SecurityUser user
    ) {
        List<ReadActivityMeResponse> result = activityService.getActivitiesMe(user.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("내 활동 로그 조회 성공", result));
    }

}
