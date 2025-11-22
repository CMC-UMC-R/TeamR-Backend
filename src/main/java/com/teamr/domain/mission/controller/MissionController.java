package com.teamr.domain.mission.controller;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.enums.DayOfWeek;
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
    public ResponseEntity<MissionResponse> createMission(MissionRequest request) {
        MissionResponse response = missionService.createMission(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{dayOfWeek}")
    public ResponseEntity<MissionRes> getMissionByDay(
            @RequestParam Long userId,
            @RequestParam DayOfWeek dayOfWeek) {
        MissionRes response = missionService.getMissionByDay(userId, dayOfWeek);
        return ResponseEntity.ok(response);
    }

}
