package com.teamr.domain.mission.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.teamr.domain.mission.enums.MissionCategory;
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
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    @Schema(description = "미션 시간 (HH:mm 형식)", example = "09:40")
    private LocalTime time;
    
    @Schema(description = "미션 설정 완료 여부 (true: 설정됨, false: 미설정)", example = "true")
    private boolean isSet;
}

