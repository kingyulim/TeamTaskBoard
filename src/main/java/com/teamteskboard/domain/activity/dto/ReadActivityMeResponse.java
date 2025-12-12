package com.teamteskboard.domain.activity.dto;

import com.teamteskboard.common.entity.Activity;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReadActivityMeResponse {

    private final Long id;
    private final Long userId;
    private final ReadActivityUserResponse user;
    private final String action;
    private final ActivityTypeEnum targetType;
    private final Long taskId;
    private final String description;
    private final LocalDateTime createdAt;

    public static ReadActivityMeResponse from(Activity activity) {
        return new ReadActivityMeResponse(
                activity.getId(),
                activity.getUser().getId(),
                ReadActivityUserResponse.from(activity.getUser()),
                activity.getType().getType(),
                activity.getType(),
                activity.getTask().getId(),
                activity.getDescription(),
                activity.getCreatedAt()
        );
    }
}
