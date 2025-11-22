package com.teamr.domain.mission.entity;


import com.teamr.domain.mission.enums.DayOfWeek;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="missions")
@Getter
@NoArgsConstructor
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType missionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionCategory missionCategory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionLog> missionLogs = new ArrayList<>();

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private PictureMission pictureMission;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private MovementMission movementMission;

    private Mission(String title, LocalTime time, MissionType missionType, MissionCategory missionCategory, DayOfWeek dayOfWeek) {
        this.title = title;
        this.time = time;
        this.missionType = missionType;
        this.missionCategory = missionCategory;
        this.dayOfWeek = dayOfWeek;
    }

    public static Mission of(String title, LocalTime time, MissionType missionType, MissionCategory missionCategory, DayOfWeek dayOfWeek) {
        return new Mission(title, time, missionType, missionCategory, dayOfWeek);
    }
}

