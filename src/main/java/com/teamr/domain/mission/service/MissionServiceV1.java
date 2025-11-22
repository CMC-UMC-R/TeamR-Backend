package com.teamr.domain.mission.service;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.entity.MovementMission;
import com.teamr.domain.mission.entity.PictureMission;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.domain.mission.exception.MissionInvalidTypeException;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.mission.repository.MovementMissionRepository;
import com.teamr.domain.mission.repository.PictureMissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionServiceV1 {

    private final MissionRepository missionRepository;
    private final PictureMissionRepository pictureMissionRepository;
    private final MovementMissionRepository movementMissionRepository;

    /**
     * 미션 생성 - 타입에 따라 PictureMission 또는 MovementMission 생성
     */
    @Transactional
    public MissionResponse createMission(MissionRequest request) {
        log.info("[MissionService] Creating mission - Category: {}, Type: {}", 
                request.getMissionCategory(), request.getMissionType());

        // 1. Mission 엔티티 생성 및 저장
        Mission savedMission = createAndSaveMission(request);

        // 2. 타입에 따라 분기 처리
        if (request.getMissionType() == MissionType.PICTURE) {
            return createPictureMission(savedMission, request.getWord());
        } else if (request.getMissionType() == MissionType.MOVEMENT) {
            return createMovementMission(savedMission, request.getCount());
        }

        log.error("MissionService] Creating mission - 지원하지 않는 미션 타입 {}", request.getMissionType());
        throw new MissionInvalidTypeException();
    }

    private Mission createAndSaveMission(MissionRequest request) {
        Mission mission = Mission.of(
                request.getTitle(),
                request.getTime(),
                request.getMissionType(),
                request.getMissionCategory(),
                request.getDayOfWeek()
        );
        
        return missionRepository.save(mission);
    }

    private MissionResponse createPictureMission(Mission mission, String word) {
        log.info("[MissionService] Creating PictureMission with word: {}", word);
        
        PictureMission pictureMission = PictureMission.of(word, mission);
        PictureMission savedPictureMission = pictureMissionRepository.save(pictureMission);
        
        log.info("[MissionService] PictureMission created successfully. ID: {}", savedPictureMission.getId());
        
        return MissionResponse.fromPicture(savedPictureMission);
    }

    private MissionResponse createMovementMission(Mission mission, Integer count) {
        log.info("[MissionService] Creating MovementMission with count: {}", count);
        
        MovementMission movementMission = MovementMission.of(count, mission);
        MovementMission savedMovementMission = movementMissionRepository.save(movementMission);
        
        log.info("[MissionService] MovementMission created successfully. ID: {}", savedMovementMission.getId());
        
        return MissionResponse.fromMovement(savedMovementMission);
    }
}
