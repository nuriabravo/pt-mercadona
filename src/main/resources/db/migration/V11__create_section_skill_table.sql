CREATE TABLE section_skill (
    section_id  BIGINT NOT NULL REFERENCES section(id),
    skill_id    BIGINT NOT NULL REFERENCES skill(id),
    PRIMARY KEY (section_id, skill_id)
);