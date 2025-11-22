package com.teamr.domain.character.repository;

import com.teamr.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.teamr.domain.character.entity.Character;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<Character, Long> {
    // User 기준 캐릭터 조회
    Optional<Character> findByUser(User user);
}
