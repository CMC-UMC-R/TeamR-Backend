package com.teamr.domain.missionlog.controller;

import com.teamr.domain.mission.enums.MissionCategory;
import com.teamr.domain.missionlog.dto.response.TodayMissionRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Mission Log", description = "미션 로그 관련 API")
public interface MissionLogSwagger {

    /**
     * 오늘의 미션 목록 조회
     * 오늘 성공한 미션 목록을 반환합니다.
     */
    @Operation(
            summary = "오늘의 미션 성공 여부 반환",
            description = "오늘 성공한 미션 목록을 반환합니다. 없을 경우 빈 값 반환"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TodayMissionRes.class))
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/today")
    ResponseEntity<List<TodayMissionRes>> getTodayMission(
            @Parameter(
                    description = "기기 고유 식별자 (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @RequestHeader("X-Device-Id") String deviceId
    );

    /**
     * 오늘의 미션 완료 처리
     * 사용자의 오늘의 미션을 성공으로 표시합니다.
     */
    @Operation(
            summary = "미션 성공 표시",
            description = "사용자의 오늘의 미션 성공으로 표시"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 완료 처리 성공",
                    content = @Content(schema = @Schema(implementation = TodayMissionRes.class))
            ),
            @ApiResponse(responseCode = "404", description = "사용자 또는 미션을 찾을 수 없음")
    })
    @PostMapping("/today/complete")
    ResponseEntity<TodayMissionRes> completeTodayMission(
            @Parameter(
                    description = "기기 고유 식별자 (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @RequestHeader("X-Device-Id") String deviceId,
            @Parameter(
                    description = "미션 카테고리 (WAKE_UP, MOVEMENT, WORK)",
                    required = true,
                    example = "WAKE_UP"
            )
            @RequestParam MissionCategory missionCategory
    );
}

