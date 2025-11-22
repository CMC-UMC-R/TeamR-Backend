package com.teamr.domain.mission.entity;

import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "picture_missions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureMission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String word;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private PictureMission(String word, Mission mission) {
        this.word = word;
        this.mission = mission;
    }

    public static PictureMission of(String word, Mission mission) {
        return new PictureMission(word, mission);
    }
}
