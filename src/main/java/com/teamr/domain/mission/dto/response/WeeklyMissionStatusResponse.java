package com.teamr.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "주간 미션 달성 현황 응답")
public class WeeklyMissionStatusResponse {

    @Schema(description = "일별 미션 달성 현황 리스트 (일요일부터 토요일까지)", example = "[...]")
    private List<DailyMissionStatus> dailyStatuses;
}

