package com.teamr.domain.user.service;

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

    /**
     * deviceId로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User findByDeviceId(String deviceId) {
        log.info("[UserService] Finding user by deviceId: {}", deviceId);
        
        return userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> {
                    log.error("[UserService] User not found - deviceId: {}", deviceId);
                    return new UserNotFoundException();
                });
    }

    /**
     * userId로 사용자 조회
     */
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        log.info("[UserService] Finding user by id: {}", userId);
        
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("[UserService] User not found - userId: {}", userId);
                    return new UserNotFoundException();
                });
    }
}

