package com.teamr.global.common;

import com.teamr.domain.mission.enums.DayOfWeekType;

import java.time.DayOfWeek;

public class DayOfWeekConverter {

    public static DayOfWeekType toDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DayOfWeekType.MONDAY;
            case TUESDAY -> DayOfWeekType.TUESDAY;
            case WEDNESDAY -> DayOfWeekType.WEDNESDAY;
            case THURSDAY -> DayOfWeekType.THURSDAY;
            case FRIDAY -> DayOfWeekType.FRIDAY;
            case SATURDAY -> DayOfWeekType.SATURDAY;
            case SUNDAY -> DayOfWeekType.SUNDAY;
        };
    }
}
