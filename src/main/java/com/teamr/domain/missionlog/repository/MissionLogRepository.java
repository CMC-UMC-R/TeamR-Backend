package com.teamr.domain.missionlog.repository;

import com.teamr.domain.missionlog.entity.MissionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionLogRepository extends JpaRepository<MissionLog, Long> {
}

