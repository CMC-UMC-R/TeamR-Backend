package com.teamr.domain.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "주간 미션 달성 현황 응답", example = """
    {
      "dailyStatuses": [
        {
          "dayOfWeek": "SUNDAY",
          "date": "2025-11-17",
          "isCompleted": true
        },
        {
          "dayOfWeek": "MONDAY",
          "date": "2025-11-18",
          "isCompleted": false
        },
        {
          "dayOfWeek": "TUESDAY",
          "date": "2025-11-19",
          "isCompleted": true
        },
        {
          "dayOfWeek": "WEDNESDAY",
          "date": "2025-11-20",
          "isCompleted": false
        },
        {
          "dayOfWeek": "THURSDAY",
          "date": "2025-11-21",
          "isCompleted": true
        },
        {
          "dayOfWeek": "FRIDAY",
          "date": "2025-11-22",
          "isCompleted": false
        },
        {
          "dayOfWeek": "SATURDAY",
          "date": "2025-11-23",
          "isCompleted": null
        }
      ]
    }
    """)
    public class WeeklyMissionStatusResponse {

    @Schema(
        description = """
                일별 미션 달성 현황 리스트 (일요일부터 토요일까지)
                - isCompleted: true = 3개 미션 모두 완료
                - isCompleted: false = 미완료 또는 일부만 완료
                - isCompleted: null = 아직 오지 않은 미래 날짜
                """,
        example = """
                [
                  {"dayOfWeek": "SUNDAY", "date": "2025-11-17", "isCompleted": true},
                  {"dayOfWeek": "MONDAY", "date": "2025-11-18", "isCompleted": false},
                  {"dayOfWeek": "TUESDAY", "date": "2025-11-19", "isCompleted": true},
                  {"dayOfWeek": "WEDNESDAY", "date": "2025-11-20", "isCompleted": false},
                  {"dayOfWeek": "THURSDAY", "date": "2025-11-21", "isCompleted": true},
                  {"dayOfWeek": "FRIDAY", "date": "2025-11-22", "isCompleted": false},
                  {"dayOfWeek": "SATURDAY", "date": "2025-11-23", "isCompleted": null}
                ]
                """)
    private List<DailyMissionStatus> dailyStatuses;
}

