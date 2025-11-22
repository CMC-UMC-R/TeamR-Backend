package com.teamr.infra.gemini.controller;

import com.teamr.global.common.MissionCategory;
import com.teamr.infra.gemini.dto.MissionVerificationResponse;
import com.teamr.infra.gemini.dto.WordGenerationRequest;
import com.teamr.infra.gemini.dto.WordGenerationResponse;
import com.teamr.infra.gemini.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/gemini")
@RequiredArgsConstructor
public class GeminiController implements GeminiSwagger {

    private final GeminiService geminiService;

    @Override
    public ResponseEntity<WordGenerationResponse> generateWord(MissionCategory category) {
        WordGenerationRequest request = WordGenerationRequest.of(category);
        WordGenerationResponse response = geminiService.generateWord(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<WordGenerationResponse> regenerateWord(
            MissionCategory category,
            List<String> existingWords) {
        WordGenerationRequest request = WordGenerationRequest.ofWithExisting(category, existingWords);
        WordGenerationResponse response = geminiService.generateWord(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MissionVerificationResponse> verifyMission(String word, MultipartFile image) {
        MissionVerificationResponse response = geminiService.verifyMission(word, image);
        return ResponseEntity.ok(response);
    }
}

