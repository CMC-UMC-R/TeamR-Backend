package com.teamr.global.common;

import com.teamr.domain.mission.enums.MissionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "Gemini API용 세부 미션 카테고리")
public enum GeminiMissionCategory {
    // 기상 미션 카테고리
    @Schema(description = "침실")
    BEDROOM(MissionCategory.WAKE_UP, "침실", "침대, 침구, 침실 풍경"),
    @Schema(description = "거실")
    LIVING_ROOM(MissionCategory.WAKE_UP, "거실", "소파, 거실 풍경, TV"),
    @Schema(description = "화장실")
    BATHROOM(MissionCategory.WAKE_UP, "화장실", "세면대, 샤워실, 거울"),
    @Schema(description = "주방")
    KITCHEN(MissionCategory.WAKE_UP, "주방", "싱크대, 냉장고, 주방 풍경"),
    @Schema(description = "옷방")
    DRESSING_ROOM(MissionCategory.WAKE_UP, "옷방", "옷장, 의류, 옷방 풍경"),
    
    // 이동 미션 카테고리
    @Schema(description = "마트 및 편의점")
    MART_CONVENIENCE(MissionCategory.MOVEMENT, "마트 및 편의점", "마트 입구, 편의점, 상점"),
    @Schema(description = "건물")
    BUILDING(MissionCategory.MOVEMENT, "건물", "건물 외관, 입구, 간판"),
    @Schema(description = "가로수")
    STREET_TREE(MissionCategory.MOVEMENT, "가로수", "나무, 가로수길, 공원"),
    @Schema(description = "도로")
    ROAD(MissionCategory.MOVEMENT, "도로", "도로, 횡단보도, 거리"),
    
    // 작업 미션 카테고리
    @Schema(description = "서재")
    STUDY_ROOM(MissionCategory.WORK, "서재", "책상, 서재, 공부 공간"),
    @Schema(description = "커피 등 음료")
    BEVERAGE(MissionCategory.WORK, "커피 등 음료", "커피, 차, 음료"),
    @Schema(description = "노트북 또는 데스크탑")
    COMPUTER(MissionCategory.WORK, "노트북 또는 데스크탑", "노트북, 데스크탑, 컴퓨터"),
    @Schema(description = "필기도구")
    WRITING_TOOLS(MissionCategory.WORK, "필기도구", "펜, 노트, 필기도구"),
    @Schema(description = "책 또는 노트")
    BOOK_NOTE(MissionCategory.WORK, "책 또는 노트", "책, 노트, 교재");

    private final MissionCategory missionCategory;
    private final String displayName;
    private final String examples;

    public String getFullDescription() {
        return String.format("%s - %s (예: %s)", 
                missionCategory.name(), 
                displayName, 
                examples);
    }
}

