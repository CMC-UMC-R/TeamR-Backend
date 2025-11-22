package com.teamr.domain.mission.exception;

import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;

public class MissionAlreadyExistsException extends BusinessException {
    public MissionAlreadyExistsException() {
        super(ErrorCode.MISSION_ALREADY_EXISTS);
    }
}

