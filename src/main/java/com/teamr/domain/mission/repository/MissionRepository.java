package com.teamr.domain.mission.repository;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    Optional<Mission> findByUserIdAndDayOfWeek(Long userId, DayOfWeek dayOfWeek);

    List<Mission> findAllByUserAndDayOfWeek(User user, DayOfWeek dayOfWeek);

}
