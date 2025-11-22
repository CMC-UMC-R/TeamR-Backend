package com.teamr.domain.mission.dto;

import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.global.common.GeminiMissionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "미션 생성 요청")
public class MissionRequest {
    
    @Schema(description = "미션 시간", example = "07:00:00")
    private final LocalTime time;
    
    @Schema(description = "미션 카테고리 (WAKE_UP, MOVEMENT, WORK)", example = "WAKE_UP")
    private final MissionCategory missionCategory;
    
    @Schema(description = """
            Gemini 세부 카테고리 (missionCategory와 일치해야 함)
            - WAKE_UP: BEDROOM, LIVING_ROOM, BATHROOM, KITCHEN, DRESSING_ROOM
            - MOVEMENT: MART_CONVENIENCE, BUILDING, STREET_TREE, ROAD
            - WORK: STUDY_ROOM, BEVERAGE, COMPUTER, WRITING_TOOLS, BOOK_NOTE
            """, 
            example = "BEDROOM")
    private final GeminiMissionCategory geminiCategory;
    
    @Schema(description = "미션 타입 (PICTURE, MOVEMENT)")
    private final MissionType missionType;
    
    @Schema(description = "요일")
    private final DayOfWeekType dayOfWeek;
    
    @Schema(description = "Gemini로 생성된 단어 (PICTURE 타입일 경우)", example = "아침 조깅", nullable = true)
    private final String word;
    
    @Schema(description = "이동 횟수 (MOVEMENT 타입일 경우)", example = "10", nullable = true)
    private final Integer count;

    public static MissionRequest of(
            LocalTime time,
            MissionCategory missionCategory,
            GeminiMissionCategory geminiCategory,
            MissionType missionType,
            DayOfWeekType dayOfWeek,
            String word,
            Integer count) {
        return new MissionRequest(time, missionCategory, geminiCategory, missionType, dayOfWeek, word, count);
    }
}

