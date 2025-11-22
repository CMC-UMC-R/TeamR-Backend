package com.teamr.domain.character.entity;

import com.teamr.domain.user.entity.User;
import com.teamr.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "characters")
@Getter
@NoArgsConstructor
public class Character extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private int count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Character(User user) {
        this.user = user;
        this.level = 1;
        this.count = 0;
    }

    public void incrementCount() {
        this.count++;
        updateLevel();
    }

    private void updateLevel() {
        CharacterLevel newLevel = CharacterLevel.getLevel(this.count);
        this.level = newLevel.getLevel();
    }
}
