INSERT INTO store (code, name) VALUES
    ('MAD01', 'Mercadona Madrid Centro'),
    ('VAL01', 'Mercadona Valencia Centro');

INSERT INTO worker (first_name, last_name, dni, contract_hours, store_id)
SELECT 'Juan', 'Pérez', '12345678A', 8, s.id FROM store s WHERE s.code = 'MAD01'
UNION ALL
SELECT 'María', 'García', '87654321B', 6, s.id FROM store s WHERE s.code = 'VAL01';

INSERT INTO store_section (store_id, section_id)
SELECT s.id, sec.id FROM store s, section sec WHERE s.code = 'MAD01' AND sec.name = 'Horno'
UNION ALL
SELECT s.id, sec.id FROM store s, section sec WHERE s.code = 'MAD01' AND sec.name = 'Cajas'
UNION ALL
SELECT s.id, sec.id FROM store s, section sec WHERE s.code = 'VAL01' AND sec.name = 'Cajas'
UNION ALL
SELECT s.id, sec.id FROM store s, section sec WHERE s.code = 'VAL01' AND sec.name = 'Verduras';

INSERT INTO worker_section_assignment (worker_id, store_section_id, assigned_hours)
SELECT w.id, ss.id, 8
FROM worker w
JOIN store s ON s.id = w.store_id
JOIN store_section ss ON ss.store_id = s.id
JOIN section sec ON sec.id = ss.section_id
WHERE w.dni = '12345678A' AND sec.name = 'Horno'
UNION ALL
SELECT w.id, ss.id, 6
FROM worker w
JOIN store s ON s.id = w.store_id
JOIN store_section ss ON ss.store_id = s.id
JOIN section sec ON sec.id = ss.section_id
WHERE w.dni = '87654321B' AND sec.name = 'Verduras';