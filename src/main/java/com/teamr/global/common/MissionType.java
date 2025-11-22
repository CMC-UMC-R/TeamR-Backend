package com.teamr.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MissionType {
    WAKEUP("기상 미션"),
    MOVEMENT("이동 미션"),
    WORK("작업 미션");

    private final String displayName;
}

