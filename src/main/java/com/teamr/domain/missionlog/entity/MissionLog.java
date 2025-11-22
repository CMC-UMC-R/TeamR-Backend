package com.teamr.domain.missionlog.entity;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.missionlog.enums.MissionStatus;
import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "mission_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus missionStatus;

    @Column(nullable = false)
    private LocalDate missionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    private MissionLog(MissionStatus missionStatus, LocalDate missionDate, Mission mission) {
        this.missionStatus = missionStatus;
        this.missionDate = missionDate;
        this.mission = mission;
    }

    public static MissionLog of(MissionStatus missionStatus, LocalDate missionDate, Mission mission) {
        return new MissionLog(missionStatus, missionDate, mission);
    }
}
