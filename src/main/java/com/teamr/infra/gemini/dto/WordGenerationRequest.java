package com.teamr.infra.gemini.dto;

import com.teamr.global.common.GeminiMissionCategory;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WordGenerationRequest {
    private final GeminiMissionCategory category;
    private final List<String> existingWords;

    public static WordGenerationRequest of(GeminiMissionCategory category) {
        return new WordGenerationRequest(category, List.of());
    }

    public static WordGenerationRequest ofWithExisting(
            GeminiMissionCategory category,
            List<String> existingWords) {
        return new WordGenerationRequest(category, existingWords);
    }
}


