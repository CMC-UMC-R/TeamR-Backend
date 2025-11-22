package com.teamr.domain.mission.exception;

import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;

public class MissionInvalidTypeException extends BusinessException {
    public MissionInvalidTypeException() {
        super(ErrorCode.MISSION_INVALID_TYPE);
    }
}
