package com.mercadona.nuriabravo.infrastructure.persistence;

import com.mercadona.nuriabravo.domain.model.Skill;
import com.mercadona.nuriabravo.domain.repository.SkillRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository.SkillJpaRepository;
import com.mercadona.nuriabravo.infrastructure.persistence.mapper.SkillJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SkillRepositoryImpl implements SkillRepository {

    private final SkillJpaRepository jpaRepository;
    private final SkillJpaMapper mapper;

    @Override
    public List<Skill> findBySectionId(Long sectionId) {
        return mapper.toDomainList(jpaRepository.findBySectionId(sectionId));
    }
}