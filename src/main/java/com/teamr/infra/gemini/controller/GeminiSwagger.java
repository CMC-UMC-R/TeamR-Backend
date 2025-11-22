package com.teamr.infra.gemini.controller;

import com.teamr.global.common.GeminiMissionCategory;
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
     * 선택한 카테고리에 맞는 대표적인 미션 단어를 AI가 생성합니다.
     */
    @Operation(
            summary = "미션 단어 생성",
            description = """
                    선택한 카테고리에 맞는 대표적인 미션 단어를 AI가 생성합니다.
                    
                    사용 가능한 카테고리:
                    
                    [기상 미션]
                    - BEDROOM (침실)
                    - LIVING_ROOM (거실)
                    - BATHROOM (화장실)
                    - KITCHEN (주방)
                    - DRESSING_ROOM (옷방)
                    
                    [이동 미션]
                    - MART_CONVENIENCE (마트/편의점)
                    - BUILDING (건물)
                    - STREET_TREE (가로수)
                    - ROAD (도로)
                    
                    [작업 미션]
                    - STUDY_ROOM (서재)
                    - BEVERAGE (커피 등 음료)
                    - COMPUTER (노트북/데스크탑)
                    - WRITING_TOOLS (필기도구)
                    - BOOK_NOTE (책/노트)
                    """
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
                    description = "미션 카테고리 (예: BEDROOM, COMPUTER, ROAD 등)",
                    required = true
            )
            @RequestParam GeminiMissionCategory category);

    /**
     * 다른 미션 단어 재생성
     * 이미 생성된 단어들과 다른 새로운 미션 단어를 AI가 생성합니다.
     */
    @Operation(
            summary = "미션 단어 재생성 (다른 단어)",
            description = """
                    생성된 단어가 마음에 들지 않을 때, 다른 새로운 단어를 AI가 생성합니다.
                    기존에 생성된 단어들을 제외하고 새로운 단어를 제안합니다.
                    
                    사용 예시:
                    - category: BEDROOM
                    - existingWords: ["침대 정리", "침구 정돈"]
                    → AI가 이 두 단어를 제외한 새로운 침실 관련 단어 생성
                    
                    사용 가능한 카테고리:
                    
                    [기상 미션]
                    - BEDROOM (침실)
                    - LIVING_ROOM (거실)
                    - BATHROOM (화장실)
                    - KITCHEN (주방)
                    - DRESSING_ROOM (옷방)
                    
                    [이동 미션]
                    - MART_CONVENIENCE (마트/편의점)
                    - BUILDING (건물)
                    - STREET_TREE (가로수)
                    - ROAD (도로)
                    
                    [작업 미션]
                    - STUDY_ROOM (서재)
                    - BEVERAGE (커피 등 음료)
                    - COMPUTER (노트북/데스크탑)
                    - WRITING_TOOLS (필기도구)
                    - BOOK_NOTE (책/노트)
                    """
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
                    description = "미션 카테고리 (예: BEDROOM, COMPUTER, ROAD 등)",
                    required = true
            )
            @RequestParam GeminiMissionCategory category,
            @Parameter(
                    description = "이미 생성된 단어 목록 (중복 제외용)",
                    required = true,
                    example = "[\"침대 정리\", \"침구 정돈\"]"
            )
            @RequestParam List<String> existingWords);

    /**
     * 미션 인증 검증
     * 사용자가 제출한 사진이 미션 단어와 일치하는지 AI가 검증합니다.
     */
    @Operation(
            summary = "미션 인증 검증",
            description = """
                    사용자가 제출한 사진이 미션 단어와 일치하는지 AI가 검증합니다.
                    
                    검증 기준:
                    - 사진이 미션 단어와 실제로 일치하는가?
                    - 사진이 명확하고 조작되지 않았는가?
                    - 미션을 실제로 수행했다고 인정할 수 있는가?
                    
                    결과:
                    - APPROVED: 인증 성공
                    - REJECTED: 인증 실패 (이유 포함)
                    
                    사용 예시:
                    - word: "침대 정리"
                    - image: 정리된 침대 사진 업로드
                    → AI가 사진을 분석하여 인증 성공/실패 판단
                    """
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
