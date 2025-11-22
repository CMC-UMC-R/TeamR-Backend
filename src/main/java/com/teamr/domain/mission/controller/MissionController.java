package com.teamr.domain.mission.controller;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.dto.response.DailyMissionResponse;
import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.mission.dto.response.WeeklyMissionStatusResponse;
import com.teamr.domain.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController implements MissionSwagger {

    private final MissionService missionService;

    @Override
    public ResponseEntity<MissionResponse> createMission(
            @RequestHeader("X-Device-Id") String deviceId,
            @RequestBody MissionRequest request
    ) {
        MissionResponse response = missionService.createMission(deviceId, request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MissionRes> getMissionByDay(String deviceId, DayOfWeekType dayOfWeek) {
        MissionRes response = missionService.getMissionByDay(deviceId, dayOfWeek);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DailyMissionResponse> getDailyMissions(String deviceId, DayOfWeekType dayOfWeek) {
        DailyMissionResponse response = missionService.getDailyMissions(deviceId, dayOfWeek);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<WeeklyMissionStatusResponse> getWeeklyMissionStatus(
            @RequestHeader("X-Device-Id") String deviceId
    ) {
        WeeklyMissionStatusResponse response = missionService.getWeeklyMissionStatus(deviceId);
        return ResponseEntity.ok(response);
    }

}
