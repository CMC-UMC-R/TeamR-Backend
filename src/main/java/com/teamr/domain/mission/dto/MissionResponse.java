package com.teamr.domain.mission.dto;

import com.teamr.domain.mission.entity.MovementMission;
import com.teamr.domain.mission.entity.PictureMission;
import com.teamr.domain.mission.enums.MissionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "미션 생성 응답")
public class MissionResponse {
    
    @Schema(description = "미션 ID")
    private final Long missionId;
    
    @Schema(description = "미션 타입")
    private final MissionType missionType;
    
    @Schema(description = "생성된 단어 (PICTURE 타입일 경우)", nullable = true)
    private final String word;
    
    @Schema(description = "이동 횟수 (MOVEMENT 타입일 경우)", nullable = true)
    private final Integer count;

    public static MissionResponse fromPicture(PictureMission pictureMission) {
        return new MissionResponse(
                pictureMission.getMission().getId(),
                MissionType.PICTURE,
                pictureMission.getWord(),
                null
        );
    }

    public static MissionResponse fromMovement(MovementMission movementMission) {
        return new MissionResponse(
                movementMission.getMission().getId(),
                MissionType.MOVEMENT,
                null,
                movementMission.getCount()
        );
    }
}

