package com.teamr.infra.gemini.exception;

import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;

public class GeminiParseFailedException extends BusinessException {
    public GeminiParseFailedException() {
        super(ErrorCode.AI_RESPONSE_PARSE_FAILED);
    }
}
