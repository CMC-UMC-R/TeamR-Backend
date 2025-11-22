package com.teamr.infra.gemini;

import com.teamr.global.exception.BusinessException;
import com.teamr.global.exception.ErrorCode;
import com.teamr.infra.gemini.dto.GeminiRequest;
import com.teamr.infra.gemini.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeminiApiClient {

    private static final String FLASH_MODEL = "gemini-2.0-flash-exp";

    private final RestClient geminiRestClient;

    @Value("${gemini.api-key}")
    private String apiKey;

    public String callApi(String prompt) {
        log.info("[Gemini API] Calling with prompt length: {}", prompt.length());

        try {
            GeminiRequest request = GeminiRequest.of(prompt);
            GeminiResponse response = executeApiCall(request);
            String extractedText = extractTextFromResponse(response);

            log.info("[Gemini API] CallApi Success - Response length: {}", extractedText.length());
            return extractedText;

        } catch (Exception e) {
            log.error("[Gemini API] Failed: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED);
        }
    }

    public String callApiWithImage(MultipartFile image, String prompt) {
        log.info("[Gemini API] Calling with image");

        try {
            String imageBase64 = convertToBase64(image);
            GeminiRequest request = GeminiRequest.ofWithImage(prompt, imageBase64);
            GeminiResponse response = executeApiCall(request);
            String extractedText = extractTextFromResponse(response);

            log.info("[Gemini API] CallApiWithImage Success - Response length: {}", extractedText.length());
            return extractedText;

        } catch (Exception e) {
            log.error("[Gemini API] Failed: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED);
        }
    }

    private String convertToBase64(MultipartFile image) {
        try {
            byte[] bytes = image.getBytes();
            return java.util.Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            log.error("[Gemini API] Failed to convert image to Base64", e);
            throw new BusinessException(ErrorCode.AI_API_CALL_FAILED);
        }
    }

    private GeminiResponse executeApiCall(GeminiRequest request) {
        return geminiRestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/models/{model}:generateContent")
                        .queryParam("key", apiKey)
                        .build(FLASH_MODEL))
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GeminiResponse.class);
    }

    private String extractTextFromResponse(GeminiResponse response) {
        try {
            return response.getCandidates().getFirst()
                    .getContent()
                    .getParts().getFirst()
                    .getText();
        } catch (Exception e) {
            log.error("[Gemini API] Failed to extract text from response", e);
            throw new BusinessException(ErrorCode.AI_RESPONSE_PARSE_FAILED);
        }
    }
}

