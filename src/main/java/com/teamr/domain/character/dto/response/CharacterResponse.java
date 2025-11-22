package com.teamr.domain.character.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterResponse {
    private int level;
    private String imageUrl;
    private int count;
}
