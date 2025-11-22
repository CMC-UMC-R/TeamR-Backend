package com.teamr.domain.mission.dto.response;

import com.teamr.domain.mission.enums.DayOfWeekType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "일별 미션 달성 현황")
public class DailyMissionStatus {

    @Schema(description = "요일", example = "MONDAY")
    private DayOfWeekType dayOfWeek;

    @Schema(description = "날짜", example = "2025-11-22")
    private LocalDate date;

    @Schema(description = "미션 완료 여부 (3개 모두 완료: true, 미완료: false, 미래: null)", example = "true")
    private Boolean isCompleted;
}

