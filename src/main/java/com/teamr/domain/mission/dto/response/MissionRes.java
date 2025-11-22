package com.teamr.domain.mission.dto.response;

import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
@Schema(description = "미션 조회 응답")
public class MissionRes {

    @Schema(description = "요일", example = "MONDAY")
    private DayOfWeek dayOfWeek;
    
    @Schema(description = "미션 시간", example = "07:00:00")
    private LocalTime time;
    
    @Schema(description = "미션 타입", example = "PICTURE")
    private MissionType missionType;

}
