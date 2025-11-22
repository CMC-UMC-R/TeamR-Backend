package com.teamr.domain.character.controller;

import com.teamr.domain.character.dto.response.CharacterResponse;
import com.teamr.domain.character.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/characters")
@RequiredArgsConstructor
public class CharacterController implements CharacterSwagger {

    private final CharacterService characterService;

    @GetMapping("/me")
    public ResponseEntity<CharacterResponse> getMyCharacter(
            @RequestHeader("X-Device-Id") String deviceId
    ) {
        return ResponseEntity.ok(characterService.getMyCharacter(deviceId));
    }
}
