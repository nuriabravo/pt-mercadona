CREATE TABLE worker_skill (
    worker_id   BIGINT NOT NULL REFERENCES worker(id),
    skill_id    BIGINT NOT NULL REFERENCES skill(id),
    PRIMARY KEY (worker_id, skill_id)
);