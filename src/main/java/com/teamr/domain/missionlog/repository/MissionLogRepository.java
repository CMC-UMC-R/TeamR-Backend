package com.teamr.domain.missionlog.repository;

import com.teamr.domain.missionlog.entity.MissionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {

    @Query("SELECT ml FROM MissionLog ml " +
            "WHERE ml.mission.user.id = :userId " +
            "AND ml.missionDate BETWEEN :startDate AND :endDate")
    List<MissionLog> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

