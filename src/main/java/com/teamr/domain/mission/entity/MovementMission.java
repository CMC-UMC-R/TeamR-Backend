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
    public void updateCount(Integer count) {
        this.count = count;
    }

}
