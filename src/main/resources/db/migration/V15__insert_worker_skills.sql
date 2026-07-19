INSERT INTO worker_skill (worker_id, skill_id)
SELECT w.id, sk.id FROM worker w, skill sk
WHERE (w.dni = '12345678A' AND sk.name IN ('Hornear Pan', 'Repostería'))
   OR (w.dni = '87654321B' AND sk.name IN ('Fortaleza física'));