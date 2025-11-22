package com.teamr.domain.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "개별 미션 정보")
public class DailyMissionDto {
    
    @Schema(description = "미션 카테고리", example = "WAKE_UP")
    private MissionCategory category;
    
    @Schema(description = "미션 카테고리 한글명", example = "기상")
    private String categoryTitle;
    
    @Schema(description = "미션 타입", example = "PICTURE", nullable = true)
    private MissionType missionType;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(description = "미션 시간 (HH:mm 형식)", example = "09:40")
    private LocalTime time;
    
    @Schema(description = "생성된 단어 (PICTURE 타입일 경우)", nullable = true, example = "사과")
    private String word;

    @Schema(description = "이동 횟수 (MOVEMENT 타입일 경우)", nullable = true, example = "10")
    private Integer count;
}

