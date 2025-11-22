package com.teamr.domain.missionlog.service;

import com.teamr.domain.missionlog.entity.MissionLog;
import com.teamr.domain.missionlog.repository.MissionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionLogService {

    private final MissionLogRepository missionLogRepository;

    /**
     * 사용자의 특정 날짜 범위 내 미션 로그 조회 및 날짜별 그룹핑
     */
    @Transactional(readOnly = true)
    public Map<LocalDate, List<MissionLog>> findAndGroupByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        log.info("[MissionLogService] Finding and grouping mission logs - userId: {}, from: {}, to: {}", 
                userId, startDate, endDate);
        
        List<MissionLog> missionLogs = missionLogRepository.findByUserIdAndDateRange(userId, startDate, endDate);
        
        log.info("[MissionLogService] Found {} mission logs, grouping by date", missionLogs.size());
        
        return missionLogs.stream()
                .collect(Collectors.groupingBy(MissionLog::getMissionDate));
    }

    /**
     * 특정 날짜의 미션 로그 개수가 목표(3개) 이상인지 확인
     */
    public boolean isCompletedForDate(LocalDate date, Map<LocalDate, List<MissionLog>> logsByDate) {
        List<MissionLog> logsForDate = logsByDate.getOrDefault(date, List.of());
        boolean isCompleted = logsForDate.size() >= 3;
        
        log.debug("[MissionLogService] Date: {}, Log count: {}, Completed: {}", 
                date, logsForDate.size(), isCompleted);
        
        return isCompleted;
    }
}

