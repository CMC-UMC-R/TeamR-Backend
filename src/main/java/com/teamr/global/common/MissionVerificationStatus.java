package com.teamr.global.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "미션 인증 상태")
public enum MissionVerificationStatus {
    @Schema(description = "인증 성공")
    APPROVED("인증 성공"),
    
    @Schema(description = "인증 실패")
    REJECTED("인증 실패");

    private final String description;
}

