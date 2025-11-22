package com.teamr.domain.mission.repository;

import com.teamr.domain.mission.entity.MovementMission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovementMissionRepository extends JpaRepository<MovementMission, Long> {
}

