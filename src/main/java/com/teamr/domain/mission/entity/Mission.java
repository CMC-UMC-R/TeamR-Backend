package com.teamr.domain.mission.entity;


import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.mission.enums.MissionType;
import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


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

    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType missionType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionCategory missionCategory;


}

