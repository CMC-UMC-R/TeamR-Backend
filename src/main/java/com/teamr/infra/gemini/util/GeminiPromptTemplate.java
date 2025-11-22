package com.teamr.infra.gemini.util;

public class GeminiPromptTemplate {

    public static String createMissionVerificationPrompt(String missionDescription) {
        return String.format("""
                당신은 기상 미션 인증을 검증하는 AI입니다.
                
                [사용자가 하겠다고 작성한 미션]
                %s
                
                [제출된 인증 사진]
                첨부된 이미지를 확인하세요.
                
                [검증 규칙]
                1. 사진이 작성한 미션 내용과 실제로 일치하는지 확인하세요.
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
                missionDescription);
    }
}
