package com.teamr.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "요일별 미션 목록 응답")
public class DailyMissionResponse {
    
    @Schema(description = "요일", example = "MONDAY")
    private String dayOfWeek;
    
    @Schema(description = "미션 목록 (기상, 이동, 활동 3개 고정)")
    private List<DailyMissionDto> missions;
}

