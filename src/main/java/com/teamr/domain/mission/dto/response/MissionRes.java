package com.teamr.domain.mission.dto.response;

import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class MissionRes {

    private DayOfWeek dayOfWeek;
    private LocalTime time;
    private MissionType missionType;

}
