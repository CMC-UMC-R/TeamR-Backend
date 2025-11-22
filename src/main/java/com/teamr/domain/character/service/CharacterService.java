package com.teamr.domain.character.service;

import com.teamr.domain.character.dto.response.CharacterResponse;
import com.teamr.domain.character.entity.Character;
import com.teamr.domain.character.entity.CharacterLevel;
import com.teamr.domain.character.repository.CharacterRepository;
import com.teamr.domain.user.entity.User;
import com.teamr.domain.user.exception.UserNotFoundException;
import com.teamr.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final UserRepository userRepository;

    @Value("${app.base-url}")
    private String baseUrl;

    /**
     * 내 캐릭터 정보 조회
     */
    @Transactional
    public CharacterResponse getMyCharacter(String deviceId) {
        // 유저 조회
        User user = userRepository.findByDeviceId(deviceId)
                .orElseGet(() -> userRepository.save(new User(deviceId)));

        Character character = characterRepository.findByUser(user)
                .orElseGet(() -> characterRepository.save(new Character(user)));

        CharacterLevel levelEnum = CharacterLevel.fromLevel(character.getLevel());
        String fullImageUrl = baseUrl + levelEnum.getImagePath();

        return CharacterResponse.builder()
                .level(character.getLevel())
                .count(character.getCount())
                .imageUrl(fullImageUrl)
                .build();
    }

    /**
     * 미션 달성 시 카운트 증가
     */
    @Transactional
    public void increaseCharacterCount(String deviceId) {
        User user = userRepository.findByDeviceId(deviceId)
                .orElseThrow(UserNotFoundException::new);

        Character character = characterRepository.findByUser(user)
                .orElseGet(() -> characterRepository.save(new Character(user)));

        character.incrementCount();
    }
}
