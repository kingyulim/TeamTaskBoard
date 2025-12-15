package com.teamteskboard.domain.activity.dto;

import com.teamteskboard.common.entity.Activity;
import com.teamteskboard.domain.activity.enums.ActivityTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ReadActivityResponse {

    private final Long id;
    private final ActivityTypeEnum type;
    private final Long userId;
    private final ReadActivityUserResponse user;
    private final Long taskId;
    private final LocalDateTime timestamp;
    private final String description;

    public static ReadActivityResponse from(Activity activity) {
        return new ReadActivityResponse(
                activity.getId(),
                activity.getType(),
                activity.getUser().getId(),
                ReadActivityUserResponse.from(activity.getUser()),
                activity.getTask().getId(),
                activity.getCreatedAt(),
                activity.getDescription()
        );
    }

}
