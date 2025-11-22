package com.teamr.infra.gemini.controller;

import com.teamr.global.common.MissionCategory;
import com.teamr.infra.gemini.dto.MissionVerificationResponse;
import com.teamr.infra.gemini.dto.WordGenerationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Gemini AI", description = "Gemini AI 관련 API")
public interface GeminiSwagger {

    /**
     * 미션 단어 생성
     * 카테고리에 맞는 대표적인 미션 단어를 AI가 생성합니다.
     */
    @Operation(
            summary = "미션 단어 생성",
            description = "선택한 카테고리에서 촬영 가능한 대표적인 미션 단어를 AI가 생성합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "단어 생성 성공",
                    content = @Content(schema = @Schema(implementation = WordGenerationResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "AI API 호출 실패")
    })
    @PostMapping("/words/generate")
    ResponseEntity<WordGenerationResponse> generateWord(
            @Parameter(
                    description = "미션 카테고리 (BEDROOM, LIVING_ROOM, BATHROOM, KITCHEN, DRESSING_ROOM, " +
                            "MART_CONVENIENCE, BUILDING, STREET_TREE, ROAD, " +
                            "STUDY_ROOM, BEVERAGE, COMPUTER, WRITING_TOOLS, BOOK_NOTE)",
                    required = true
            )
            @RequestParam MissionCategory category);

    /**
     * 다른 미션 단어 생성
     * 이미 생성된 단어들과 다른 새로운 미션 단어를 AI가 생성합니다.
     */
    @Operation(
            summary = "다른 미션 단어 재생성",
            description = "이미 생성된 단어들과 다른 새로운 미션 단어를 AI가 생성합니다. 마음에 들지 않을 때 사용하세요."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "단어 생성 성공",
                    content = @Content(schema = @Schema(implementation = WordGenerationResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "AI API 호출 실패")
    })
    @PostMapping("/words/regenerate")
    ResponseEntity<WordGenerationResponse> regenerateWord(
            @Parameter(
                    description = "미션 카테고리",
                    required = true
            )
            @RequestParam MissionCategory category,
            @Parameter(
                    description = "이미 생성된 단어 목록 (중복 방지)",
                    required = true,
                    example = "[\"침대\", \"베개\"]"
            )
            @RequestParam List<String> existingWords);

    /**
     * 미션 인증 검증
     * 사용자가 선택한 단어와 제출한 사진이 일치하는지 AI가 검증합니다.
     */
    @Operation(
            summary = "미션 인증 검증",
            description = "사용자가 선택한 단어와 제출한 사진이 일치하는지 AI가 검증합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "검증 완료",
                    content = @Content(schema = @Schema(implementation = MissionVerificationResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 입력값"),
            @ApiResponse(responseCode = "500", description = "AI API 호출 실패")
    })
    @PostMapping(value = "/verify", consumes = "multipart/form-data")
    ResponseEntity<MissionVerificationResponse> verifyMission(
            @Parameter(description = "미션 단어", required = true)
            @RequestParam String word,
            @Parameter(description = "이미지 파일", required = true)
            @RequestPart("image") MultipartFile image);
}
