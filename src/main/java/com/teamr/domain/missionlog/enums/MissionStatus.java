package com.teamr.domain.missionlog.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionStatus {
    SUCCESS("성공"),
    FAILED("실패"),
    PENDING("대기중");

    private final String description;
}

