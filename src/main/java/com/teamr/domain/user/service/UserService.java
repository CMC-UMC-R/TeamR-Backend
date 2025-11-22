package com.teamr.domain.user.service;

import com.teamr.domain.mission.repository.MissionRepository;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.user.exception.UserNotFoundException;
import com.teamr.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;

    /**
     * deviceId로 사용자 조회 (없으면 자동 생성)
     */
    @Transactional(readOnly = true)
    public User findByDeviceId(String deviceId) {
        log.info("[UserService] Finding user by deviceId: {}", deviceId);
        return userRepository.findByDeviceId(deviceId)
                .orElseGet(() -> userRepository.save(new User(deviceId)));
    }

    /**
     * deviceId로 사용자 및 관련 데이터 삭제
     * 1. User의 Mission들을 먼저 삭제 (CASCADE로 MissionLog, PictureMission, MovementMission 자동 삭제)
     * 2. User 삭제
     */
    @Transactional
    public void deleteByDeviceId(String deviceId) {
        log.info("[UserService] Deleting user by deviceId: {}", deviceId);
        
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> {
                    log.error("[UserService] User not found for deletion - deviceId: {}", deviceId);
                    return new UserNotFoundException();
                });
        
        // Mission 삭제 (CASCADE로 관련 데이터 자동 삭제)
        long deletedMissionCount = missionRepository.deleteByUser(user);
        log.info("[UserService] Deleted {} missions for user: {}", deletedMissionCount, user.getId());
        
        // User 삭제
        userRepository.delete(user);
        
        log.info("[UserService] User deleted successfully - userId: {}, deviceId: {}", user.getId(), deviceId);
    }
}

