package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;

import com.mercadona.nuriabravo.infrastructure.persistence.jpa.SkillJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class SkillJpaRepositoryTest {

    @Autowired
    private SkillJpaRepository skillJpaRepository;

    @Test
    void findBySectionId_shouldReturnSkillsLinkedToSection() {
        List<SkillJpa> skills = skillJpaRepository.findBySectionId(1L);

        assertThat(skills).extracting(SkillJpa::getName)
                .containsExactlyInAnyOrder("Hornear Pan", "Repostería");
    }
}