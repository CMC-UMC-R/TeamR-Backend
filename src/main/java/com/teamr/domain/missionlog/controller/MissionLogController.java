package com.teamr.domain.missionlog.controller;

import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.missionlog.dto.response.TodayMissionRes;
import com.teamr.domain.missionlog.service.MissionLogService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "오늘의 미션 성공 여부 반환", description = "오늘 성공한 미션 목록을 반환합니다. 없을 경우 빈 값 반환")
    public ResponseEntity<List<TodayMissionRes>> getTodayMission(@RequestHeader("X-Device-Id") String deviceId) {

        List<TodayMissionRes> todayMissions = missionLogService.getTodayMission(deviceId);
        return ResponseEntity.ok(todayMissions);
    }

    //오늘의 미션 완료 처리
    @PostMapping("/today/complete")
    @Operation(summary = "미션 성공 표시", description = "사용자의 오늘의 미션 성공으로 표시")
    public ResponseEntity<TodayMissionRes> completeTodayMission(
            @RequestHeader("X-Device-Id") String deviceId,
            @RequestParam MissionCategory missionCategory) {
        TodayMissionRes result = missionLogService.updateTodayMission(deviceId, missionCategory);
        return ResponseEntity.ok(result);
    }
}
