package com.teamr.infra.gemini.prompt;

import java.util.List;

public class GeminiPromptTemplate {

    public static String createWordGenerationPrompt(String category) {
        return String.format("""
                당신은 기상 미션 단어를 생성하는 AI입니다.
                
                [사용자가 입력한 카테고리]
                %s
                
                [요구사항]
                1. 해당 카테고리에 어울리는 대표적인 단어를 하나 생성하세요.
                2. 단어는 간단하고 명확해야 합니다 (1~3단어).
                3. 사진으로 인증 가능한 구체적인 활동이어야 합니다.
                
                [응답 형식]
                다음 JSON 형식으로만 응답하세요:
                {
                  "word": "생성된 단어",
                  "description": "단어에 대한 간단한 설명 (한 문장)"
                }
                
                예시:
                - 카테고리가 "운동"이면 → "아침 조깅", "스쿼트 30회" 등
                - 카테고리가 "공부"면 → "영어 단어 암기", "독서 30분" 등
                - 카테고리가 "습관"이면 → "물 마시기", "명상 10분" 등
                
                단어를 생성해주세요.
                """,
                category);
    }

    public static String createDifferentWordPrompt(
            String category,
            List<String> existingWords) {
        return String.format("""
                당신은 기상 미션 단어를 생성하는 AI입니다.
                
                [사용자가 입력한 카테고리]
                %s
                
                [이미 생성된 단어들 (중복 금지)]
                %s
                
                [요구사항]
                1. 위 단어들과 다른 새로운 단어를 생성하세요.
                2. 해당 카테고리에 어울리는 대표적인 단어여야 합니다.
                3. 단어는 간단하고 명확해야 합니다 (1~3단어).
                4. 사진으로 인증 가능한 구체적인 활동이어야 합니다.
                
                [응답 형식]
                다음 JSON 형식으로만 응답하세요:
                {
                  "word": "생성된 단어",
                  "description": "단어에 대한 간단한 설명 (한 문장)"
                }
                
                새로운 단어를 생성해주세요.
                """,
                category,
                String.join(", ", existingWords));
    }

    public static String createVerificationPrompt(String word) {
        return String.format("""
                당신은 미션 인증을 검증하는 AI입니다.
                
                [미션 단어]
                %s
                
                [제출된 인증 사진]
                첨부된 이미지를 확인하세요.
                
                [검증 규칙]
                1. 사진이 미션 단어와 실제로 일치하는지 확인하세요.
                2. 사진이 명확하고 조작되지 않았는지 확인하세요.
                3. 사진 속 상황/장소/행동이 미션을 수행했다고 인정할 수 있는지 판단하세요.
                4. 미션을 실제로 수행한 것이 확실하면 APPROVED, 의심스럽거나 무관하면 REJECTED를 반환하세요.
                
                [응답 형식]
                다음 JSON 형식으로만 응답하세요:
                {
                  "status": "APPROVED" 또는 "REJECTED",
                  "reason": "판단 이유를 한 문장으로 설명"
                }
                
                판단해주세요.
                """,
                word);
    }
}

