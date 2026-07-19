package com.mercadona.nuriabravo.infrastructure.persistence.mapper;

import com.mercadona.nuriabravo.domain.model.Skill;
import com.mercadona.nuriabravo.infrastructure.persistence.jpa.SkillJpa;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillJpaMapper {
    Skill toDomain(SkillJpa entity);
    List<Skill> toDomainList(List<SkillJpa> entities);
}
