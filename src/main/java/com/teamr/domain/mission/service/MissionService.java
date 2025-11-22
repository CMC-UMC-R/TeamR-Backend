package com.teamr.domain.mission.service;

import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.dto.response.MovementMissionRes;
import com.teamr.domain.mission.dto.resquest.MovementMissionReq;
import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.entity.MovementMission;
import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.mission.repository.MovementMissionRepository;
import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;
    private final MovementMissionRepository movementMissionRepository;

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

    @Transactional
    public MovementMissionRes setMovementMission(Long userId, MovementMissionReq request) {

        Mission mission = missionRepository.findByUserIdAndDayOfWeek(userId, request.getDayOfWeek())
                .map(existingMission -> {
                    existingMission.update(request.getMissionType(), request.getTime());
                    return existingMission;
                })
                .orElseGet(() -> {
                    Mission newMission = Mission.builder()
                            .dayOfWeek(request.getDayOfWeek())
                            .missionType(request.getMissionType())
                            .time(request.getTime())
                            .build();
                    return missionRepository.save(newMission);
                });

        MovementMission movementMission = movementMissionRepository.findByMission(mission)
                .map(existingMovementMission -> {
                    existingMovementMission.updateCount(request.getCount());
                    return existingMovementMission;
                })
                .orElseGet(() -> {
                    MovementMission newMovementMission = MovementMission.builder()
                            .mission(mission)
                            .count(request.getCount())
                            .build();
                    return movementMissionRepository.save(newMovementMission);
                });


        return MovementMissionRes.builder()
                .dayOfWeek(mission.getDayOfWeek())
                .missionType(mission.getMissionType())
                .time(mission.getTime())
                .count(movementMission.getCount())
                .build();
    }

}


