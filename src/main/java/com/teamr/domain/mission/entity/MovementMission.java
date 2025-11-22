package com.teamr.domain.mission.entity;

import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movement_missions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovementMission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private MovementMission(Integer count, Mission mission) {
        this.count = count;
        this.mission = mission;
    }

    public static MovementMission of(Integer count, Mission mission) {
        return new MovementMission(count, mission);
    }
}
