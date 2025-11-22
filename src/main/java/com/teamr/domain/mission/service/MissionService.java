package com.teamr.domain.mission.service;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.dto.response.DailyMissionDto;
import com.teamr.domain.mission.dto.response.DailyMissionResponse;
import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.entity.MovementMission;
import com.teamr.domain.mission.entity.PictureMission;
import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.domain.mission.exception.MissionInvalidTypeException;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.mission.repository.MovementMissionRepository;
import com.teamr.domain.mission.repository.PictureMissionRepository;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.user.exception.UserNotFoundException;
import com.teamr.domain.user.repository.UserRepository;
import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public MissionRes getMissionByDay(String deviceId, DayOfWeek dayOfWeek) {
        // 유저 조회
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(UserNotFoundException::new);

        Mission mission = missionRepository.findByUserIdAndDayOfWeek(user.getId(), dayOfWeek)
                .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));

        return MissionRes.builder()
                .dayOfWeek(mission.getDayOfWeek())
                .time(mission.getTime())
                .missionType(mission.getMissionType())
                .build();
    }

    /**
     * 요일별 미션 목록 조회 (3개 고정: 기상, 이동, 활동)
     * DB에 데이터가 없어도 빈 껍데기(00:00, isSet: false)를 포함하여 항상 3개 반환
     */
    @Transactional(readOnly = true)
    public DailyMissionResponse getDailyMissions(String deviceId, DayOfWeek dayOfWeek) {
        log.info("[MissionService] Getting daily missions - DeviceId: {}, DayOfWeek: {}", deviceId, dayOfWeek);

        // 유저 조회
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(UserNotFoundException::new);

        // 해당 요일의 저장된 미션 가져오기
        List<Mission> savedMissions = missionRepository.findAllByUserAndDayOfWeek(user, dayOfWeek);
        log.info("[MissionService] Found {} saved missions", savedMissions.size());

        // UI에 보여줄 3가지 카테고리 정의 (순서 중요)
        List<MissionCategory> targetCategories = List.of(
                MissionCategory.WAKE_UP,
                MissionCategory.MOVEMENT,
                MissionCategory.WORK
        );

        // 저장된 미션과 고정 카테고리를 매핑하여 응답 생성
        List<DailyMissionDto> missionDtos = targetCategories.stream()
                .map(category -> {
                    // DB에 해당 카테고리가 있는지 찾음
                    return savedMissions.stream()
                            .filter(m -> m.getMissionCategory() == category)
                            .findFirst()
                            .map(this::toDto) // 있으면 있는 정보로 변환
                            .orElseGet(() -> createEmptyDto(category)); // 없으면 빈 껍데기 생성
                })
                .collect(Collectors.toList());

        log.info("[MissionService] Returning {} mission DTOs", missionDtos.size());

        return DailyMissionResponse.builder()
                .dayOfWeek(dayOfWeek.name())
                .missions(missionDtos)
                .build();
    }

    /**
     * 존재하는 미션을 DTO로 변환
     */
    private DailyMissionDto toDto(Mission mission) {
        return DailyMissionDto.builder()
                .category(mission.getMissionCategory())
                .categoryTitle(getKoreanTitle(mission.getMissionCategory()))
                .time(mission.getTime())
                .isSet(true) // DB에 있으면 설정된 것
                .build();
    }

    /**
     * DB에 없을 때 보여줄 빈 껍데기 (00:00, isSet: false)
     */
    private DailyMissionDto createEmptyDto(MissionCategory category) {
        return DailyMissionDto.builder()
                .category(category)
                .categoryTitle(getKoreanTitle(category))
                .time(LocalTime.of(0, 0)) // 00:00
                .isSet(false) // 설정 안 됨
                .build();
    }

    /**
     * 카테고리별 한글 타이틀 매핑
     */
    private String getKoreanTitle(MissionCategory category) {
        return switch (category) {
            case WAKE_UP -> "기상";
            case MOVEMENT -> "이동";
            case WORK -> "활동";
        };
    }


}


