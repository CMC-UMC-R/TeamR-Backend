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
public class MissionLogController {

    private final MissionLogService missionLogService;

    //오늘의 미션 목록 조회
    @GetMapping("/today")
    public ResponseEntity<List<TodayMissionRes>> getTodayMission(@RequestHeader("X-Device-Id") String deviceId) {

        List<TodayMissionRes> todayMissions = missionLogService.getTodayMission(deviceId);
        return ResponseEntity.ok(todayMissions);
    }

    //오늘의 미션 완료 처리
    @PostMapping("/today/complete")
    public ResponseEntity<TodayMissionRes> completeTodayMission(
            @RequestHeader("X-Device-Id") String deviceId,
            @RequestParam MissionCategory missionCategory) {
        TodayMissionRes result = missionLogService.updateTodayMission(deviceId, missionCategory);
        return ResponseEntity.ok(result);
    }
}
