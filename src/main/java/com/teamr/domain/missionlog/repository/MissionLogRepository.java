package com.teamr.domain.missionlog.repository;

import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.domain.missionlog.enums.MissionStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {
    List<MissionLog> findByMission_User_DeviceIdAndMissionStatusAndCreatedAtBetween(String deviceId, MissionStatus missionStatus, LocalDateTime start, LocalDateTime end);

    Optional<MissionLog> findByMission_User_DeviceIdAndMission_MissionCategoryAndCreatedAtBetween(String deviceId, MissionCategory missionCategory, LocalDateTime start, LocalDateTime end);

    @Query("SELECT ml FROM MissionLog ml " +
            "WHERE ml.mission.user.id = :userId " +
            "AND ml.missionDate BETWEEN :startDate AND :endDate")
    List<MissionLog> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

