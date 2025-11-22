package com.teamr.domain.usermission.entity;

import com.teamr.domain.mission.entity.Mission;
import com.teamr.domain.user.entity.User;
import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="user_missions")
@Getter
@NoArgsConstructor
public class UserMission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    @Column(nullable = false)
    private Boolean isCompleted = false;

}
