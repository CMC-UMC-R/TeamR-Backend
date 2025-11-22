package com.teamr.domain.missionlog.service;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.missionlog.dto.response.TodayMissionRes;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.domain.missionlog.enums.MissionStatus;
import com.teamr.domain.missionlog.repository.MissionLogRepository;
import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionLogService {

    private final MissionLogRepository missionLogRepository;
    private final MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public List<TodayMissionRes> getTodayMission(String deviceId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        // 오늘 완료한 미션 로그 조회
        Set<MissionCategory> completedCategories = missionLogRepository.findByMission_User_DeviceIdAndMissionStatusAndCreatedAtBetween(deviceId, MissionStatus.SUCCESS, startOfDay, endOfDay)
                .stream()
                .map(missionLog -> missionLog.getMission().getMissionCategory())
                .collect(Collectors.toSet());

        // 모든 미션 카테고리에 대해 완료 여부를 포함하여 반환
        return Arrays.stream(MissionCategory.values())
                .map(category -> TodayMissionRes.builder()
                        .missionCategory(category)
                        .missionStatus(completedCategories.contains(category) ? MissionStatus.SUCCESS : MissionStatus.PENDING)
                        .build())
                .collect(Collectors.toList());
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
