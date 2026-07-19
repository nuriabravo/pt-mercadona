package com.mercadona.nuriabravo.domain.repository;

import com.mercadona.nuriabravo.domain.model.Skill;

import java.util.List;

public interface SkillRepository {
    List<Skill> findBySectionId(Long sectionId);
}