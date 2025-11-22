package com.teamr.domain.mission.controller;

import com.teamr.domain.mission.dto.MissionRequest;
import com.teamr.domain.mission.dto.MissionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
}
