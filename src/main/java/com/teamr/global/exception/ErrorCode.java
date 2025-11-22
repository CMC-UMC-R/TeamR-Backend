package com.teamr.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // Common (C001~)
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", "지원하지 않는 HTTP 메서드입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다"),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C004", "잘못된 타입입니다"),
    
    // Auth (A001~)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "A002", "권한이 없습니다"),
    
    // User (U001~)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "U002", "이미 존재하는 사용자입니다"),

    // Mission (M001~)
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "미션을 찾을 수 없습니다"),
    MISSION_INVALID_TYPE(HttpStatus.BAD_REQUEST, "M002", "유효하지 않은 미션 타입입니다"),
    MISSION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "M003", "해당 요일에 이미 미션이 존재합니다"),

    // AI/External API (E001~)
    AI_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "AI API 호출에 실패했습니다"),
    AI_RESPONSE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "E002", "AI 응답 파싱에 실패했습니다"),
    AI_TIMEOUT(HttpStatus.REQUEST_TIMEOUT, "E003", "AI API 응답 시간이 초과되었습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

