package com.teamr.domain.missionlog.service;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.missionlog.dto.response.TodayMissionRes;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.domain.missionlog.enums.MissionStatus;
import com.teamr.domain.missionlog.repository.MissionLogRepository;
import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionLogService {

    private final MissionLogRepository missionLogRepository;
    private final MissionRepository missionRepository;

    /**
     * 사용자의 특정 날짜 범위 내 미션 로그 조회 및 날짜별 그룹핑
     */
    @Transactional(readOnly = true)
    public Map<LocalDate, List<MissionLog>> findAndGroupByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        log.info("[MissionLogService] Finding and grouping mission logs - userId: {}, from: {}, to: {}",
                userId, startDate, endDate);

        List<MissionLog> missionLogs = missionLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);

        log.info("[MissionLogService] Found {} mission logs, grouping by date", missionLogs.size());

        return missionLogs.stream()
                .collect(Collectors.groupingBy(MissionLog::getMissionDate));
    }

    /**
     * 특정 날짜의 미션 로그 개수가 목표(3개) 이상인지 확인
     */
    public boolean isCompletedForDate(LocalDate date, Map<LocalDate, List<MissionLog>> logsByDate) {
        List<MissionLog> logsForDate = logsByDate.getOrDefault(date, List.of());
        boolean isCompleted = logsForDate.size() >= 3;

        log.debug("[MissionLogService] Date: {}, Log count: {}, Completed: {}",
                date, logsForDate.size(), isCompleted);

        return isCompleted;
    }


    @Transactional(readOnly = true)
    public List<TodayMissionRes> getTodayMission(String deviceId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        LocalTime currentTime = LocalTime.now();

        // 오늘 완료한 미션 로그 조회
        Set<MissionCategory> completedCategories = missionLogRepository.findByMission_User_DeviceIdAndMissionStatusAndCreatedAtBetween(
                        deviceId, MissionStatus.SUCCESS, startOfDay, endOfDay)
                .stream()
                .map(missionLog -> missionLog.getMission().getMissionCategory())
                .collect(Collectors.toSet());

        // 오늘 요일의 미션들을 카테고리별로 맵핑
        DayOfWeek javaDayOfWeek = today.getDayOfWeek();
        Map<MissionCategory, Mission> missionsByCategory = missionRepository
                .findByUser_DeviceIdAndDayOfWeek(deviceId, convertToDayOfWeekType(javaDayOfWeek))
                .stream()
                .collect(Collectors.toMap(Mission::getMissionCategory, mission -> mission));

        // 모든 미션 카테고리에 대해 상태 판단
        return Arrays.stream(MissionCategory.values())
                .map(category -> TodayMissionRes.builder()
                        .missionCategory(category)
                        .missionStatus(determineMissionStatus(category, completedCategories, missionsByCategory, currentTime))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 미션 카테고리별 상태 판단
     * - SUCCESS: 오늘 완료한 로그가 있는 경우
     * - FAILED: 로그가 없고, 미션 시간에서 10분이 지난 경우
     * - PENDING: 로그가 없고, 미션 시간이 현재 시간보다 늦거나 미션이 없는 경우
     */
    private MissionStatus determineMissionStatus(
            MissionCategory category,
            Set<MissionCategory> completedCategories,
            Map<MissionCategory, Mission> missionsByCategory,
            LocalTime currentTime
    ) {
        // 완료한 경우
        if (completedCategories.contains(category)) {
            return MissionStatus.SUCCESS;
        }

        // 해당 카테고리의 미션이 있는지 확인
        Mission mission = missionsByCategory.get(category);
        if (mission == null) {
            // 미션이 설정되지 않은 경우
            return MissionStatus.PENDING;
        }

        LocalTime missionTime = mission.getTime();
        LocalTime missionTimeWith10Minutes = missionTime.plusMinutes(10);

        // 현재 시간이 미션 시간 + 10분을 넘었으면 FAILED
        if (currentTime.isAfter(missionTimeWith10Minutes)) {
            return MissionStatus.FAILED;
        }

        // 미션 시간이 현재 시간보다 늦으면 PENDING
        if (missionTime.isAfter(currentTime)) {
            return MissionStatus.PENDING;
        }

        // 미션 시간이 지났지만 10분 이내면 PENDING (아직 수행 가능)
        return MissionStatus.PENDING;
    }

    /**
     * java.time.DayOfWeek를 DayOfWeekType으로 변환
     */
    private DayOfWeekType convertToDayOfWeekType(java.time.DayOfWeek javaDayOfWeek) {
        return switch (javaDayOfWeek) {
            case MONDAY -> com.teamr.domain.mission.enums.DayOfWeekType.MONDAY;
            case TUESDAY -> com.teamr.domain.mission.enums.DayOfWeekType.TUESDAY;
            case WEDNESDAY -> com.teamr.domain.mission.enums.DayOfWeekType.WEDNESDAY;
            case THURSDAY -> com.teamr.domain.mission.enums.DayOfWeekType.THURSDAY;
            case FRIDAY -> com.teamr.domain.mission.enums.DayOfWeekType.FRIDAY;
            case SATURDAY -> com.teamr.domain.mission.enums.DayOfWeekType.SATURDAY;
            case SUNDAY -> com.teamr.domain.mission.enums.DayOfWeekType.SUNDAY;
        };
    }

    @Transactional
    public TodayMissionRes updateTodayMission(String deviceId, MissionCategory missionCategory) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        // 1. 오늘 이미 해당 미션을 성공했는지 확인
        missionLogRepository.findByMission_User_DeviceIdAndMission_MissionCategoryAndCreatedAtBetween(deviceId, missionCategory, startOfDay, endOfDay)
                .ifPresent(log -> {
                    throw new BusinessException(ErrorCode.ALREADY_COMPLETED_MISSION);
                });

        // 2. 미션 정보 조회
        Mission mission = missionRepository.findByUser_DeviceIdAndMissionCategory(deviceId, missionCategory)
                .orElseThrow(() -> new BusinessException(ErrorCode.MISSION_NOT_FOUND));

        // 3. MissionLog 생성 및 저장 (missionStatus = SUCCESS)
        MissionLog newMissionLog = MissionLog.builder()
                .mission(mission)
                .missionStatus(MissionStatus.SUCCESS)
                .missionDate(today)
                .build();
        missionLogRepository.save(newMissionLog);

        // 4. 결과 반환
        return TodayMissionRes.builder()
                .missionCategory(missionCategory)
                .missionStatus(MissionStatus.SUCCESS)
                .build();
    }
}
