package com.mercadona.nuriabravo.infrastructure.persistence.jpa.repository;


import com.mercadona.nuriabravo.infrastructure.persistence.jpa.SkillJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillJpaRepository extends JpaRepository<SkillJpa, Long> {

    @Query(value = """
        SELECT sk.* FROM skill sk
        INNER JOIN section_skill ss ON ss.skill_id = sk.id
        WHERE ss.section_id = :sectionId
        """, nativeQuery = true)
    List<SkillJpa> findBySectionId(@Param("sectionId") Long sectionId);
}
