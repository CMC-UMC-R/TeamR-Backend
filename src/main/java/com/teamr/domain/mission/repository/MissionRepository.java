package com.teamr.domain.mission.repository;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByUserIdAndDayOfWeek(Long userId, DayOfWeek dayOfWeek);

    Optional<Mission> findByUser_DeviceIdAndMissionCategory(String deviceId, MissionCategory missionCategory);
}
