package com.teamr.domain.mission.controller;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import com.teamr.domain.mission.dto.response.DailyMissionResponse;
import com.teamr.domain.mission.dto.response.MissionRes;
import com.teamr.domain.mission.enums.DayOfWeek;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Mission", description = "미션 관련 API")
public interface MissionSwagger {

    /**
     * 미션 생성 (PICTURE 또는 MOVEMENT 타입)
     * 미션 타입에 따라 PictureMission 또는 MovementMission을 생성합니다.
     * 이미 존재하는 미션(같은 userId, dayOfWeek)이면 BadRequest 발생
     */
    @Operation(
            summary = "미션 생성",
            description = """
                    미션을 생성합니다.
                    - PICTURE 타입: Gemini로 생성된 단어(word)가 필요
                    - MOVEMENT 타입: 이동 횟수(count)가 필요
                    
                    미션 카테고리:
                    - WAKE_UP (기상 미션): PICTURE 타입
                    - MOVEMENT (이동 미션): MOVEMENT 타입
                    - WORK (작업 미션): PICTURE 타입
                    
                    주의사항:
                    - 동일한 사용자의 동일한 요일에 이미 미션이 존재하면 400 에러 발생
                    - 미션 수정(update)은 지원하지 않습니다
                    - Gemini API는 별도로 호출하여 word를 생성한 후 이 API를 호출해야 합니다
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 생성 성공",
                    content = @Content(schema = @Schema(implementation = MissionResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값 또는 이미 존재하는 미션"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping
    ResponseEntity<MissionResponse> createMission(@RequestBody MissionRequest request);

    /**
     * 요일별 미션 조회
     * 특정 사용자의 특정 요일에 설정된 미션 정보를 조회합니다.
     */
    @Operation(
            summary = "요일별 미션 조회",
            description = "사용자 ID와 요일을 기준으로 미션 시간, 종류, 카테고리를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 조회 성공",
                    content = @Content(schema = @Schema(implementation = MissionRes.class))
            ),
            @ApiResponse(responseCode = "404", description = "미션을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/{dayOfWeek}")
    ResponseEntity<MissionRes> getMissionByDay(
            @Parameter(description = "사용자 ID", required = true, example = "1")
            @RequestParam Long userId,
            @Parameter(description = "요일", required = true, example = "MONDAY")
            @RequestParam DayOfWeek dayOfWeek
    );

    @Operation(
            summary = "요일별 미션 목록 조회 (3개 고정)",
            description = "특정 요일의 미션 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "미션 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DailyMissionResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("/daily/{dayOfWeek}")
    ResponseEntity<DailyMissionResponse> getDailyMissions(
            @Parameter(
                    description = "기기 고유 식별자 (UUID)",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @RequestHeader("X-Device-Id") String deviceId,
            @Parameter(description = "요일 (MONDAY ~ SUNDAY)", required = true, example = "MONDAY")
            @PathVariable DayOfWeek dayOfWeek
    );
}
