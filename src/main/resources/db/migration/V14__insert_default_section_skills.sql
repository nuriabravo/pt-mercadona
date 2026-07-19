INSERT INTO section_skill (section_id, skill_id)
SELECT s.id, sk.id FROM section s, skill sk
WHERE (s.name = 'Horno' AND sk.name IN ('Hornear Pan', 'Repostería'))
   OR (s.name = 'Cajas' AND sk.name IN ('Simpatía', 'Matemáticas'))
   OR (s.name = 'Pescadería' AND sk.name IN ('Manejo de armas blancas', 'Limpiar pescado'))
   OR (s.name = 'Verduras' AND sk.name IN ('Fortaleza física'))
   OR (s.name = 'Droguería' AND sk.name IN ('Alquimia'));