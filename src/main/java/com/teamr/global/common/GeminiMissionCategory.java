package com.teamr.global.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "미션 카테고리")
public enum GeminiMissionCategory {
    // 기상 미션 카테고리
    BEDROOM(MissionType.WAKEUP, "침실", "침대, 침구, 침실 풍경"),
    LIVING_ROOM(MissionType.WAKEUP, "거실", "소파, 거실 풍경, TV"),
    BATHROOM(MissionType.WAKEUP, "화장실", "세면대, 샤워실, 거울"),
    KITCHEN(MissionType.WAKEUP, "주방", "싱크대, 냉장고, 주방 풍경"),
    DRESSING_ROOM(MissionType.WAKEUP, "옷방", "옷장, 의류, 옷방 풍경"),
    
    // 이동 미션 카테고리
    MART_CONVENIENCE(MissionType.MOVEMENT, "마트 및 편의점", "마트 입구, 편의점, 상점"),
    BUILDING(MissionType.MOVEMENT, "건물", "건물 외관, 입구, 간판"),
    STREET_TREE(MissionType.MOVEMENT, "가로수", "나무, 가로수길, 공원"),
    ROAD(MissionType.MOVEMENT, "도로", "도로, 횡단보도, 거리"),
    
    // 작업 미션 카테고리
    STUDY_ROOM(MissionType.WORK, "서재", "책상, 서재, 공부 공간"),
    BEVERAGE(MissionType.WORK, "커피 등 음료", "커피, 차, 음료"),
    COMPUTER(MissionType.WORK, "노트북 또는 데스크탑", "노트북, 데스크탑, 컴퓨터"),
    WRITING_TOOLS(MissionType.WORK, "필기도구", "펜, 노트, 필기도구"),
    BOOK_NOTE(MissionType.WORK, "책 또는 노트", "책, 노트, 교재");

    private final MissionType missionType;
    private final String displayName;
    private final String examples;

    public String getFullDescription() {
        return String.format("%s - %s (예: %s)", 
                missionType.getDisplayName(), 
                displayName, 
                examples);
    }
}

