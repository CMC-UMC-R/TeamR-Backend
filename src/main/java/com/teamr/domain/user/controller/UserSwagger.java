package com.teamr.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "User", description = "사용자 관련 API")
public interface UserSwagger {

    /**
     * 사용자 탈퇴 (모든 데이터 삭제)
     * deviceId로 사용자를 찾아 관련된 모든 데이터를 삭제합니다.
     */
    @Operation(
            summary = "[테스트용 API]사용자 탈퇴",
            description = """
                    deviceId로 사용자를 찾아 관련된 모든 데이터를 삭제합니다.
                    
                    삭제되는 데이터:
                    - User (사용자 정보)
                    - Mission (사용자의 모든 미션)
                    - MissionLog (미션 수행 기록)
                    - PictureMission (사진 미션 상세)
                    - MovementMission (이동 미션 상세)
                    
                    주의사항:
                    - 삭제된 데이터는 복구할 수 없습니다
                    - CASCADE 옵션으로 관련 데이터가 자동으로 함께 삭제됩니다
                    
                    인증:
                    - deviceId를 헤더로 전달하여 사용자 식별
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "사용자 탈퇴 성공 (No Content)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류"
            )
    })
    @DeleteMapping
    ResponseEntity<Void> deleteUser(
            @Parameter(
                    description = "기기 고유 식별자 (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @RequestHeader("X-Device-Id") String deviceId
    );
}

