package com.teamr.domain.mission.dto;

import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "미션 생성 요청")
public class MissionRequest {
    
    @Schema(description = "미션 제목", example = "아침 조깅하기")
    private final String title;
    
    @Schema(description = "미션 시간", example = "07:00:00")
    private final LocalTime time;
    
    @Schema(description = "미션 카테고리 (WAKE_UP, MOVEMENT, WORK)")
    private final MissionCategory missionCategory;
    
    @Schema(description = "미션 타입 (PICTURE, MOVEMENT)")
    private final MissionType missionType;
    
    @Schema(description = "요일")
    private final DayOfWeek dayOfWeek;
    
    @Schema(description = "Gemini로 생성된 단어 (PICTURE 타입일 경우)", example = "아침 조깅", nullable = true)
    private final String word;
    
    @Schema(description = "이동 횟수 (MOVEMENT 타입일 경우)", example = "10", nullable = true)
    private final Integer count;

    public static MissionRequest of(
            String title,
            LocalTime time,
            MissionCategory missionCategory,
            MissionType missionType,
            DayOfWeek dayOfWeek,
            String word,
            Integer count) {
        return new MissionRequest(title, time, missionCategory, missionType, dayOfWeek, word, count);
    }
}

