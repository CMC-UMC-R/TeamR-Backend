package com.teamr.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionVerificationStatus {
    APPROVED("인증 성공"),
    REJECTED("인증 실패");

    private final String description;
}

