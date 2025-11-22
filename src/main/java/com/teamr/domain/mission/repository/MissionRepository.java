package com.teamr.domain.mission.repository;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.mission.enums.MissionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByUserIdAndDayOfWeek(Long userId, DayOfWeekType dayOfWeek);

    @Query("SELECT m FROM Mission m " +
            "LEFT JOIN FETCH m.pictureMission " +
            "LEFT JOIN FETCH m.movementMission " +
            "WHERE m.user = :user AND m.dayOfWeek = :dayOfWeek")
    List<Mission> findAllByUserAndDayOfWeek(@Param("user") User user, @Param("dayOfWeek") DayOfWeekType dayOfWeek);

    Optional<Mission> findByUser_DeviceIdAndMissionCategory(String deviceId, MissionCategory missionCategory);
}
