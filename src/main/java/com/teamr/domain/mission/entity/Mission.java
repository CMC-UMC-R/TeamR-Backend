package com.teamr.domain.mission.entity;


import com.teamr.domain.mission.enums.DayOfWeekType;
import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="missions")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private DayOfWeekType dayOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MissionLog> missionLogs = new ArrayList<>();

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private PictureMission pictureMission;

    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    private MovementMission movementMission;

    public void update(MissionType missionType, LocalTime time) {
        this.missionType = missionType;
        this.time = time;
    }

    private Mission(User user, LocalTime time, MissionType missionType, MissionCategory missionCategory, DayOfWeekType dayOfWeek) {
        this.user = user;
        this.time = time;
        this.missionType = missionType;
        this.missionCategory = missionCategory;
        this.dayOfWeek = dayOfWeek;
    }

    public static Mission of(User user, LocalTime time, MissionType missionType, MissionCategory missionCategory, DayOfWeekType dayOfWeek) {
        return new Mission(user, time, missionType, missionCategory, dayOfWeek);
    }
}

