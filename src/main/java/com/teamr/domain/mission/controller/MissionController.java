package com.teamr.domain.mission.controller;

import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.dto.response.MovementMissionRes;
import com.teamr.domain.mission.dto.resquest.MovementMissionReq;
import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.service.MissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/{dayOfWeek}")
    public ResponseEntity<MissionRes> getMissionByDay(
            @RequestParam Long userId,
            @RequestParam DayOfWeek dayOfWeek) {
        MissionRes response = missionService.getMissionByDay(userId, dayOfWeek);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/movement-mission")
    public ResponseEntity<MovementMissionRes> setMovementMission(
            @RequestParam Long userId,
            @RequestBody @Valid MovementMissionReq request) {
        MovementMissionRes response = missionService.setMovementMission(userId, request);
        return ResponseEntity.ok(response);
    }

}
