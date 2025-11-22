package com.teamr.domain.character.controller;

import com.teamr.domain.character.dto.response.CharacterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Character", description = "캐릭터 관련 API")
public interface CharacterSwagger {

    /**
     * 내 캐릭터 조회
     * 사용자의 현재 캐릭터 정보를 조회합니다.
     */
    @Operation(
            summary = "내 캐릭터 조회",
            description = "로그인한 사용자의 현재 캐릭터 레벨, 이미지, 미션 완료 횟수를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "캐릭터 조회 성공",
                    content = @Content(schema = @Schema(implementation = CharacterResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/me")
    ResponseEntity<CharacterResponse> getMyCharacter(
            @Parameter(
                    description = "기기 고유 식별자 (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @RequestHeader("X-Device-Id") String deviceId
    );
}

