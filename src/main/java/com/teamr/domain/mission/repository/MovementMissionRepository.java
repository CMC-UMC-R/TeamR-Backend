package com.teamr.domain.mission.repository;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.entity.MovementMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovementMissionRepository extends JpaRepository<MovementMission, Long> {

    Optional<MovementMission> findByMission(Mission mission);

}

