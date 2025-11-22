package com.teamr.domain.mission.dto.resquest;

import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MovementMissionReq {

    @NotNull
    private DayOfWeekType dayOfWeek;
    @NotNull
    private MissionType missionType;
    @NotNull
    private MissionCategory missionCategory;

    @NotNull
    private LocalTime time;
    @NotNull
    private Integer count;

}
