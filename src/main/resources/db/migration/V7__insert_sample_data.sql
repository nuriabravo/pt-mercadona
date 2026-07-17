INSERT INTO store (code, name) VALUES
    ('MAD01', 'Mercadona Madrid Centro'),
    ('VAL01', 'Mercadona Valencia Centro');

INSERT INTO worker (first_name, last_name, dni, contract_hours, store_id) VALUES
    ('Juan', 'Pérez', '12345678A', 8, 1),
    ('María', 'García', '87654321B', 6, 2);

INSERT INTO store_section (store_id, section_id) VALUES
    (1, 1), -- Madrid - Horno
    (1, 2), -- Madrid - Cajas
    (2, 2), -- Valencia - Cajas
    (2, 4); -- Valencia - Verduras

INSERT INTO worker_section_assignment (worker_id, store_section_id, assigned_hours) VALUES
    (1, 1, 8), -- Juan - Horno Madrid
    (2, 4, 6); -- María - Verduras Valencia