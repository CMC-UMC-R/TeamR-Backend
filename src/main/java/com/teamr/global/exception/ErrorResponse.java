package com.teamr.global.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
@Schema(description = "에러 응답 객체")
public class ErrorResponse {

    @Schema(description = "에러 코드", example = "C003")
    private final String code;

    @Schema(description = "에러 메시지", example = "서버 내부 오류가 발생했습니다")
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "필드별 상세 에러 메시지", example = "{\"email\": \"이메일 형식이 올바르지 않습니다\"}")
    private final Map<String, String> errors;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
        this.errors = null;
    }

    public ErrorResponse(String code, String message, Map<String, String> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
    }
}

