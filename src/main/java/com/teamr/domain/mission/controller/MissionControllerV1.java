package com.teamr.domain.mission.controller;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.service.MissionServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/missions")
@RequiredArgsConstructor
@Slf4j
public class MissionControllerV1 implements MissionSwagger {
    
    private final MissionServiceV1 missionServiceV1;

    @Override
    public ResponseEntity<MissionResponse> createMission(MissionRequest request) {
        MissionResponse response = missionServiceV1.createMission(request);
        return ResponseEntity.ok(response);
    }
}
