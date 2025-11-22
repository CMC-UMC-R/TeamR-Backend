package com.teamr.domain.missionlog.repository;

import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.domain.missionlog.enums.MissionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {

    List<MissionLog> findByMission_User_DeviceIdAndMissionStatusAndCreatedAtBetween(String deviceId, MissionStatus missionStatus, LocalDateTime start, LocalDateTime end);

    Optional<MissionLog> findByMission_User_DeviceIdAndMission_MissionCategoryAndCreatedAtBetween(String deviceId, MissionCategory missionCategory, LocalDateTime start, LocalDateTime end);

}
