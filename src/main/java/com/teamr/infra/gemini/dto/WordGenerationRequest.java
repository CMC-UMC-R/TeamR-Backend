package com.teamr.infra.gemini.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WordGenerationRequest {
    private final String category;
    private final List<String> existingWords;

    public static WordGenerationRequest of(String category) {
        return new WordGenerationRequest(category, List.of());
    }

    public static WordGenerationRequest ofWithExisting(
            String category,
            List<String> existingWords) {
        return new WordGenerationRequest(category, existingWords);
    }
}

