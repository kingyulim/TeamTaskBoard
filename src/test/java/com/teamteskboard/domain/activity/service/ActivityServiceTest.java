package com.teamteskboard.domain.activity.service;

import com.teamteskboard.common.entity.Activity;
import com.teamteskboard.common.entity.Task;
import com.teamteskboard.common.entity.User;
import com.teamteskboard.domain.activity.dto.ReadActivityResponse;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import com.teamteskboard.domain.activity.repository.ActivityRepository;
import com.teamteskboard.domain.task.enums.TaskPriorityEnum;
import com.teamteskboard.domain.user.enums.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;
    @InjectMocks
    private ActivityService activityService;

    @Test
    @DisplayName("전체 활동로그 조회 시 파라미터에 입력한대로 조회한다")
    void getActivities_success() {
        // Given
        ActivityTypeEnum type = ActivityTypeEnum.TASK_CREATED;
        Long taskId = 1L;
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDateTime start = startDate.atStartOfDay();
        LocalDate endDate = LocalDate.of(2025, 12, 31);
        LocalDateTime end = endDate.atTime(LocalTime.MAX);
        Pageable pageable = PageRequest.of(0, 10);

        User user = new User("권민서", "minss", "minss@gmail.com", "1234", UserRoleEnum.USER);
        Task task = new Task("팀프로젝트", "아웃소싱", TaskPriorityEnum.MEDIUM, user, LocalDateTime.now());
        ReflectionTestUtils.setField(task, "id", taskId);

        Activity activity = new Activity(task, user, type, "");
        Page<Activity> activityPage = new PageImpl<>(List.of(activity));
        given(activityRepository.findActivities(pageable, type, taskId, start, end)).willReturn(activityPage);

        // When
        Page<ReadActivityResponse> Activities = activityService.getActivities(type, taskId, startDate, endDate, pageable);

        // Then
        assertEquals(type, Activities.getContent().get(0).getType());

    }
}