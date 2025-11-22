package com.teamr.domain.user.controller;

import com.teamr.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController implements UserSwagger {
    
    private final UserService userService;

    @Override
    public ResponseEntity<Void> deleteUser(@RequestHeader("X-Device-Id") String deviceId) {
        userService.deleteByDeviceId(deviceId);
        return ResponseEntity.noContent().build();
    }
}
