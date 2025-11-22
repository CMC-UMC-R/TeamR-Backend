package com.teamr.infra.gemini.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import com.teamr.infra.gemini.GeminiApiClient;
import com.teamr.infra.gemini.dto.MissionVerificationResponse;
import com.teamr.infra.gemini.dto.WordGenerationRequest;
import com.teamr.infra.gemini.dto.WordGenerationResponse;
import com.teamr.infra.gemini.prompt.GeminiPromptTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

    private final GeminiApiClient geminiApiClient;
    private final ObjectMapper objectMapper;

    public WordGenerationResponse generateWord(WordGenerationRequest request) {
        log.info("[Gemini Service] Generating word for category: {}", request.getCategory());

        String prompt = request.getExistingWords().isEmpty()
                ? GeminiPromptTemplate.createWordGenerationPrompt(request.getCategory())
                : GeminiPromptTemplate.createDifferentWordPrompt(
                        request.getCategory(),
                        request.getExistingWords());

        String response = geminiApiClient.callApi(prompt);
        String cleanedJson = cleanJsonResponse(response);

        return parseJsonResponse(cleanedJson, WordGenerationResponse.class);
    }

    public MissionVerificationResponse verifyMission(String word, MultipartFile image) {
        log.info("[Gemini Service] Verifying mission for word: {}", word);

        String prompt = GeminiPromptTemplate.createVerificationPrompt(word);
        String response = geminiApiClient.callApiWithImage(image, prompt);
        String cleanedJson = cleanJsonResponse(response);

        return parseJsonResponse(cleanedJson, MissionVerificationResponse.class);
    }

    private String cleanJsonResponse(String response) {
        if (response == null || response.trim().isEmpty()) {
            throw new BusinessException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
        }

        String cleaned = response.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }

        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }

        return cleaned.trim();
    }

    private <T> T parseJsonResponse(String json, Class<T> responseType) {
        try {
            return objectMapper.readValue(json, responseType);
        } catch (Exception e) {
            log.error("[Gemini Service] Failed to parse JSON response", e);
            throw new BusinessException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
        }
    }
}
