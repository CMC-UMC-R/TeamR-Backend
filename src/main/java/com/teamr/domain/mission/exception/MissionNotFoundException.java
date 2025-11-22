package com.teamr.domain.mission.exception;

import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;

public class MissionNotFoundException extends BusinessException {
    public MissionNotFoundException() {
        super(ErrorCode.MISSION_NOT_FOUND);
    }
}

