package com.teamr.domain.mission.dto.response;

import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class MovementMissionRes {

    private DayOfWeek dayOfWeek;
    private MissionType missionType;
    private LocalTime time;
    private Integer count;

}
