package com.teamr.domain.missionlog.dto.response;

import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.missionlog.enums.MissionStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TodayMissionRes {


    private MissionCategory missionCategory;
    private MissionStatus missionStatus;

}
