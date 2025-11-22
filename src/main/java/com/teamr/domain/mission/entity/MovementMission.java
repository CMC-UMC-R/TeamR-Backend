package com.teamr.domain.mission.entity;

import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "movement_missions")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
