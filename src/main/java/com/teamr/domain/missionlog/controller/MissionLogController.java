package com.teamr.domain.missionlog.controller;

import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.missionlog.dto.response.TodayMissionRes;
import com.teamr.domain.missionlog.service.MissionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mission-logs")
@RequiredArgsConstructor
public class MissionLogController implements MissionLogSwagger {

    private final MissionLogService missionLogService;

    @Override
    public ResponseEntity<List<TodayMissionRes>> getTodayMission(String deviceId) {
        List<TodayMissionRes> todayMissions = missionLogService.getTodayMission(deviceId);
        return ResponseEntity.ok(todayMissions);
    }

    @Override
    public ResponseEntity<TodayMissionRes> completeTodayMission(String deviceId, MissionCategory missionCategory) {
        TodayMissionRes result = missionLogService.updateTodayMission(deviceId, missionCategory);
        return ResponseEntity.ok(result);
    }
}
