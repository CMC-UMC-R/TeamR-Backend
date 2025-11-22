package com.teamr.infra.gemini.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.teamr.global.common.MissionVerificationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionVerificationResponse {
    
    @JsonProperty("status")
    private MissionVerificationStatus status;
    
    @JsonProperty("reason")
    private String reason;
}

