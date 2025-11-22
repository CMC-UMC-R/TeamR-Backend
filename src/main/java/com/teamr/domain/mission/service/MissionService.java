package com.teamr.domain.mission.service;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.dto.response.DailyMissionDto;
import com.teamr.domain.mission.dto.response.DailyMissionResponse;
import com.teamr.domain.mission.dto.response.DailyMissionStatus;
import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.dto.response.WeeklyMissionStatusResponse;
import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.entity.MovementMission;
import com.teamr.domain.mission.entity.PictureMission;
import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.domain.mission.exception.MissionAlreadyExistsException;
import com.teamr.domain.mission.exception.MissionInvalidTypeException;
import com.teamr.domain.mission.exception.MissionNotFoundException;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.mission.repository.MovementMissionRepository;
import com.teamr.domain.mission.repository.PictureMissionRepository;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.domain.missionlog.service.MissionLogService;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.user.service.UserService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final UserService userService;
    private final MissionLogService missionLogService;
    /**
     * 미션 생성 - 타입에 따라 PictureMission 또는 MovementMission 생성
     * 이미 존재하는 미션이면 BadRequest 발생
     */
    @Transactional
    public MissionResponse createMission(String deviceId, MissionRequest request) {
        log.info("[MissionService] Creating mission - DeviceId: {}, DayOfWeek: {}, Category: {}, Type: {}",
                deviceId, request.getDayOfWeek(), request.getMissionCategory(), request.getMissionType());

        // 1. deviceId로 User 조회
        User user = userService.findByDeviceId(deviceId);

        // 2. 이미 존재하는 미션인지 확인 (같은 요일, 같은 카테고리면 중복)
        validateMissionNotExists(user.getId(), request.getDayOfWeek(), request.getMissionCategory());

        // 3. Mission 엔티티 생성 및 저장
        Mission savedMission = createAndSaveMission(user, request);

        // 4. 타입에 따라 분기 처리
        if (request.getMissionType() == MissionType.PICTURE) {
            return createPictureMission(savedMission, request.getWord());
        } else if (request.getMissionType() == MissionType.MOVEMENT) {
            return createMovementMission(savedMission, request.getCount());
        }

        log.error("[MissionService] Creating mission - 지원하지 않는 미션 타입 {}", request.getMissionType());
        throw new MissionInvalidTypeException();
    }

    /**
     * 같은 요일, 같은 카테고리의 미션이 이미 존재하는지 확인
     * 카테고리별로 1개씩만 생성 가능
     */
    private void validateMissionNotExists(Long userId, DayOfWeekType dayOfWeek, MissionCategory missionCategory) {
        missionRepository.findByUserIdAndDayOfWeekAndMissionCategory(userId, dayOfWeek, missionCategory)
                .ifPresent(mission -> {
                    log.error("[MissionService] Mission already exists - UserId: {}, DayOfWeek: {}, Category: {}", 
                            userId, dayOfWeek, missionCategory);
                    throw new MissionAlreadyExistsException();
                });
    }

    private Mission createAndSaveMission(User user, MissionRequest request) {
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
    public MissionRes getMissionByDay(String deviceId, DayOfWeekType dayOfWeek) {
        log.info("[MissionService] Getting mission by day - DeviceId: {}, DayOfWeek: {}", deviceId, dayOfWeek);

        User user = userService.findByDeviceId(deviceId);

        Mission mission = missionRepository.findByUserIdAndDayOfWeek(user.getId(), dayOfWeek)
                .orElseThrow(() -> {
                    log.error("[MissionService] Mission not found - UserId: {}, DayOfWeek: {}", user.getId(), dayOfWeek);
                    return new MissionNotFoundException();
                });

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
    public DailyMissionResponse getDailyMissions(String deviceId, DayOfWeekType dayOfWeek) {
        log.info("[MissionService] Getting daily missions - DeviceId: {}, DayOfWeek: {}", deviceId, dayOfWeek);

        // 유저 조회
        User user = userService.findByDeviceId(deviceId);

        // 해당 요일의 저장된 미션 가져오기
        List<Mission> savedMissions = missionRepository.findAllByUserAndDayOfWeek(user, dayOfWeek);
        log.info("[MissionService] Found {} saved missions", savedMissions.size());

        // UI에 보여줄 3가지 카테고리 정의
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
                .toList();

        log.info("[MissionService] Returning {} mission DTOs", missionDtos.size());

        return DailyMissionResponse.builder()
                .dayOfWeek(dayOfWeek.name())
                .missions(missionDtos)
                .build();
    }

    /**
     * 존재하는 미션을 DTO로 변환
     * MissionType에 따라 MovementMission 또는 PictureMission 정보를 함께 반환
     */
    private DailyMissionDto toDto(Mission mission) {
        // PICTURE 타입: word만 채움
        if (mission.getMissionType() == MissionType.PICTURE && mission.getPictureMission() != null) {
            return DailyMissionDto.builder()
                    .category(mission.getMissionCategory())
                    .categoryTitle(getKoreanTitle(mission.getMissionCategory()))
                    .missionType(mission.getMissionType())
                    .time(mission.getTime())
                    .word(mission.getPictureMission().getWord())
                    .count(null)
                    .build();
        }
        
        // MOVEMENT 타입: count만 채움
        if (mission.getMissionType() == MissionType.MOVEMENT && mission.getMovementMission() != null) {
            return DailyMissionDto.builder()
                    .category(mission.getMissionCategory())
                    .categoryTitle(getKoreanTitle(mission.getMissionCategory()))
                    .missionType(mission.getMissionType())
                    .time(mission.getTime())
                    .word(null)
                    .count(mission.getMovementMission().getCount())
                    .build();
        }

        // 기본값 (타입이 없거나 데이터가 없을 경우)
        return DailyMissionDto.builder()
                .category(mission.getMissionCategory())
                .categoryTitle(getKoreanTitle(mission.getMissionCategory()))
                .missionType(mission.getMissionType())
                .time(mission.getTime())
                .word(null)
                .count(null)
                .build();
    }

    /**
     * DB에 없을 때 보여줄 빈 껍데기
     */
    private DailyMissionDto createEmptyDto(MissionCategory category) {
        return DailyMissionDto.builder()
                .category(category)
                .categoryTitle(getKoreanTitle(category))
                .missionType(null)
                .time(LocalTime.of(0, 0))
                .word(null)
                .count(null)
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


    /**
     * 이번 주 미션 달성 현황 조회
     * deviceId로 사용자를 식별하고, 이번 주(일요일~토요일) 미션 완료 현황을 반환
     * 각 날짜당 3개의 미션이 모두 완료되면 true, 아니면 false, 미래 날짜는 null
     */
    @Transactional(readOnly = true)
    public WeeklyMissionStatusResponse getWeeklyMissionStatus(String deviceId) {
        log.info("[MissionService] Fetching weekly mission status for deviceId: {}", deviceId);

        // 1. deviceId로 User 조회
        User user = userService.findByDeviceId(deviceId);

        // 2. 이번 주의 시작일(일요일)과 종료일(토요일) 계산
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SATURDAY));

        log.info("[MissionService] Week range: {} to {}", startOfWeek, endOfWeek);

        // 3. 이번 주 날짜 범위의 MissionLog 조회 및 그룹핑
        Map<LocalDate, List<MissionLog>> logsByDate = missionLogService.findAndGroupByDateRange(
                user.getId(), startOfWeek, endOfWeek
        );

        // 4. 일요일부터 토요일까지 순회하며 DailyMissionStatus 생성
        List<DailyMissionStatus> dailyStatuses = buildDailyStatuses(startOfWeek, endOfWeek, today, logsByDate);

        log.info("[MissionService] Weekly mission status retrieved successfully for user: {}", user.getId());

        return WeeklyMissionStatusResponse.builder()
                .dailyStatuses(dailyStatuses)
                .build();
    }

    /**
     * 주간 일별 미션 상태 리스트 생성
     */
    private List<DailyMissionStatus> buildDailyStatuses(
            LocalDate startOfWeek,
            LocalDate endOfWeek,
            LocalDate today,
            Map<LocalDate, List<MissionLog>> logsByDate
    ) {
        List<DailyMissionStatus> dailyStatuses = new ArrayList<>();
        LocalDate currentDate = startOfWeek;

        while (!currentDate.isAfter(endOfWeek)) {
            DayOfWeekType dayOfWeek = convertJavaDayOfWeekToCustom(currentDate.getDayOfWeek());
            Boolean isCompleted = calculateDailyCompletionStatus(currentDate, today, logsByDate);

            dailyStatuses.add(DailyMissionStatus.builder()
                    .dayOfWeek(dayOfWeek)
                    .date(currentDate)
                    .isCompleted(isCompleted)
                    .build());

            currentDate = currentDate.plusDays(1);
        }

        return dailyStatuses;
    }

    /**
     * 특정 날짜의 미션 완료 여부 계산
     * - 미래 날짜: null
     * - 과거/오늘: 로그가 3개 이상이면 true, 아니면 false
     */
    private Boolean calculateDailyCompletionStatus(
            LocalDate targetDate,
            LocalDate today,
            Map<LocalDate, List<MissionLog>> logsByDate
    ) {
        if (targetDate.isAfter(today)) {
            return null;
        }

        return missionLogService.isCompletedForDate(targetDate, logsByDate);
    }

    /**
     * java.time.DayOfWeek를 커스텀 DayOfWeek enum으로 변환
     */
    private DayOfWeekType convertJavaDayOfWeekToCustom(DayOfWeek javaDayOfWeek) {
        return switch (javaDayOfWeek) {
            case MONDAY -> DayOfWeekType.MONDAY;
            case TUESDAY -> DayOfWeekType.TUESDAY;
            case WEDNESDAY -> DayOfWeekType.WEDNESDAY;
            case THURSDAY -> DayOfWeekType.THURSDAY;
            case FRIDAY -> DayOfWeekType.FRIDAY;
            case SATURDAY -> DayOfWeekType.SATURDAY;
            case SUNDAY -> DayOfWeekType.SUNDAY;
        };
    }
}


