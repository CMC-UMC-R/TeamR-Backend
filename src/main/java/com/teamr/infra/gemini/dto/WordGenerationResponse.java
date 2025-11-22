package com.teamr.infra.gemini.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WordGenerationResponse {
    
    @JsonProperty("word")
    private String word;
    
    @JsonProperty("description")
    private String description;
}

