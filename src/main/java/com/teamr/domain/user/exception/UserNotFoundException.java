package com.teamr.domain.user.exception;

import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
