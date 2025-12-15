package com.teamteskboard.domain.activity.service;

import com.teamteskboard.domain.activity.dto.ReadActivityMeResponse;
import com.teamteskboard.domain.activity.dto.ReadActivityResponse;
import com.teamteskboard.common.entity.Activity;
import com.teamteskboard.domain.activity.repository.ActivityRepository;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import com.teamteskboard.common.exception.CustomException;
import com.teamteskboard.common.exception.ExceptionMessageEnum;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    /**
     * 전체 활동 로그 조회
     * @param type 활동 타입
     * @param taskId 작업 ID
     * @param startDate 시작일
     * @param endDate 종료일
     * @param pageable 페이징 정보를 담고 있는 객체
     * @return 전체 활동 로그 (페이징)
     */
    public Page<ReadActivityResponse> getActivities(
            ActivityTypeEnum type, Long taskId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime start = startDate == null ? null : startDate.atStartOfDay(); // 시간을 00:00:00으로 설정
        LocalDateTime end = endDate == null ? null : endDate.atTime(LocalTime.MAX); // 시간을 23:59:59로 설정

        // 활동 로그 조회
        Page<Activity> activities = activityRepository.findActivities(pageable, type, taskId, start, end);
        return activities.map(ReadActivityResponse::from);
    }

    /**
     * 내 활동 로그 조회
     * @param id 로그인한 사용자의 id
     * @return 내 활동 로그
     */
    public List<ReadActivityMeResponse> getActivitiesMe(Long id) {
        // 사용자 조회
        User user = userRepository.findById(id)
                .orElseThrow(()->new CustomException(ExceptionMessageEnum.NOT_FOUND_USER));

        // 내 활동 로그 조회
        List<Activity> activities = activityRepository.findAllByUser(user);
        return activities.stream().map(ReadActivityMeResponse::from).toList();
    }
}
