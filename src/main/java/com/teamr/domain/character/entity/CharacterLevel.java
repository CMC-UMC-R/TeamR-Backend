package com.teamr.domain.character.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CharacterLevel {

    // 레벨 1 (0~2회)
    LEVEL_1(1, 0, "/images/char_lv1.png"),

    // 레벨 2 (3~20회)
    LEVEL_2(2, 3, "/images/char_lv2.png"),

    // 레벨 3 (21회 이상)
    LEVEL_3(3, 21, "/images/char_lv3.png");

    private final int level;
    private final int requiredClears; // 레벨 달성에 필요한 최소 횟수
    private final String imagePath;

    public static CharacterLevel getLevel(int currentClears) {
        // 레벨 3
        if (currentClears >= LEVEL_3.requiredClears) {
            return LEVEL_3;
        }

        // 레벨 2
        if (currentClears >= LEVEL_2.requiredClears) {
            return LEVEL_2;
        }

        return LEVEL_1;
    }

    public static CharacterLevel fromLevel(int level) {
        for (CharacterLevel cl : values()) {
            if (cl.level == level) {
                return cl;
            }
        }
        return LEVEL_1;
    }
}
