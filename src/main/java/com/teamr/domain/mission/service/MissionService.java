package com.teamr.domain.mission.service;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.entity.MovementMission;
import com.teamr.domain.mission.entity.PictureMission;
import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.domain.mission.exception.MissionInvalidTypeException;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.mission.repository.MovementMissionRepository;
import com.teamr.domain.mission.repository.PictureMissionRepository;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.user.repository.UserRepository;
import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {

    private final MissionRepository missionRepository;
    private final PictureMissionRepository pictureMissionRepository;
    private final MovementMissionRepository movementMissionRepository;
    private final UserRepository userRepository;

    /**
     * 미션 생성 - 타입에 따라 PictureMission 또는 MovementMission 생성
     * 이미 존재하는 미션이면 BadRequest 발생
     */
    @Transactional
    public MissionResponse createMission(MissionRequest request) {
        log.info("[MissionService] Creating mission - UserId: {}, DayOfWeek: {}, Category: {}, Type: {}",
                request.getUserId(), request.getDayOfWeek(), request.getMissionCategory(), request.getMissionType());

        // 1. 이미 존재하는 미션인지 확인 (update 불가)
        validateMissionNotExists(request.getUserId(), request.getDayOfWeek());

        // 2. Mission 엔티티 생성 및 저장
        Mission savedMission = createAndSaveMission(request);

        // 3. 타입에 따라 분기 처리
        if (request.getMissionType() == MissionType.PICTURE) {
            return createPictureMission(savedMission, request.getWord());
        } else if (request.getMissionType() == MissionType.MOVEMENT) {
            return createMovementMission(savedMission, request.getCount());
        }

        log.error("[MissionService] Creating mission - 지원하지 않는 미션 타입 {}", request.getMissionType());
        throw new MissionInvalidTypeException();
    }

    private void validateMissionNotExists(Long userId, DayOfWeek dayOfWeek) {
        missionRepository.findByUserIdAndDayOfWeek(userId, dayOfWeek)
                .ifPresent(mission -> {
                    log.error("[MissionService] Mission already exists - UserId: {}, DayOfWeek: {}", userId, dayOfWeek);
                    throw new BusinessException(ErrorCode.MISSION_ALREADY_EXISTS);
                });
    }

    private Mission createAndSaveMission(MissionRequest request) {
        // User 조회
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        // Mission 생성
        Mission mission = Mission.of(
                user,
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
    // 요일별 미션 시간, 종류, 카테고리 조회
    @Transactional(readOnly = true)
    public MissionRes getMissionByDay(Long userId, DayOfWeek dayOfWeek) {
        Mission mission = missionRepository.findByUserIdAndDayOfWeek(userId, dayOfWeek)
                .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));

        return MissionRes.builder()
                .dayOfWeek(mission.getDayOfWeek())
                .time(mission.getTime())
                .missionType(mission.getMissionType())
                .build();
    }


}


