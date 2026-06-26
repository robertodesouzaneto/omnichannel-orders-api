-- =============================================================
-- V7__seed.sql  —  Initial seed data for testing
-- =============================================================

-- -------------------------------------------------------------
-- UNIT
-- -------------------------------------------------------------
INSERT INTO units (id, name, address) VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'Unidade Centro', 'Rua do Nordeste, 100 – Centro')
ON CONFLICT (id) DO NOTHING;
-- -------------------------------------------------------------
-- CATEGORIES
-- -------------------------------------------------------------
INSERT INTO categories (id, name, description) VALUES
    ('cccccccc-cccc-cccc-cccc-000000000001', 'Pratos Principais', 'Refeicoes completas nordestinas'),
    ('cccccccc-cccc-cccc-cccc-000000000002', 'Entradas',          'Petiscos e aperitivos regionais'),
    ('cccccccc-cccc-cccc-cccc-000000000003', 'Bebidas',           'Sucos, vitaminas e refrigerantes'),
    ('cccccccc-cccc-cccc-cccc-000000000004', 'Sobremesas',        'Doces e sobremesas tipicas')
ON CONFLICT (id) DO NOTHING;

-- -------------------------------------------------------------
-- PRODUCTS
-- -------------------------------------------------------------
INSERT INTO products (id, name, description, price, category_id) VALUES
    -- Pratos Principais
    ('dddddddd-dddd-dddd-dddd-000000000001', 'Baiao de Dois',       'Arroz com feijao-de-corda, charque e coalho',    32.90, 'cccccccc-cccc-cccc-cccc-000000000001'),
    ('dddddddd-dddd-dddd-dddd-000000000002', 'Carne de Sol',        'Carne curada com manteiga da terra e macaxeira', 45.90, 'cccccccc-cccc-cccc-cccc-000000000001'),
    ('dddddddd-dddd-dddd-dddd-000000000003', 'Frango ao Molho',     'Frango caipira ao molho de tomate caseiro',      38.50, 'cccccccc-cccc-cccc-cccc-000000000001'),
    -- Entradas
    ('dddddddd-dddd-dddd-dddd-000000000004', 'Macaxeira Frita',     'Macaxeira crocante com molho de pimenta',        18.90, 'cccccccc-cccc-cccc-cccc-000000000002'),
    ('dddddddd-dddd-dddd-dddd-000000000005', 'Queijo Coalho Grelhado', 'Coalho na brasa com mel de engenho',          22.00, 'cccccccc-cccc-cccc-cccc-000000000002'),
    -- Bebidas
    ('dddddddd-dddd-dddd-dddd-000000000006', 'Suco de Caju',        'Suco natural de caju gelado 500ml',              12.00, 'cccccccc-cccc-cccc-cccc-000000000003'),
    ('dddddddd-dddd-dddd-dddd-000000000007', 'Vitamina de Acerola', 'Vitamina cremosa de acerola com leite 400ml',    14.00, 'cccccccc-cccc-cccc-cccc-000000000003'),
    -- Sobremesas
    ('dddddddd-dddd-dddd-dddd-000000000008', 'Pudim de Leite',      'Pudim artesanal com calda de caramelo',          16.00, 'cccccccc-cccc-cccc-cccc-000000000004')
ON CONFLICT (id) DO NOTHING;

-- -------------------------------------------------------------
-- STOCK  (todos os produtos na Filial Centro)
-- -------------------------------------------------------------
INSERT INTO stock (id, unit_id, product_id, quantity) VALUES
    ('eeeeeeee-eeee-eeee-eeee-000000000001', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000001', 2),
    ('eeeeeeee-eeee-eeee-eeee-000000000002', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000002', 3),
    ('eeeeeeee-eeee-eeee-eeee-000000000003', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000003', 4),
    ('eeeeeeee-eeee-eeee-eeee-000000000004', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000004', 2),
    ('eeeeeeee-eeee-eeee-eeee-000000000005', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000005', 5),
    ('eeeeeeee-eeee-eeee-eeee-000000000006', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000006', 6),
    ('eeeeeeee-eeee-eeee-eeee-000000000007', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000007', 7),
    ('eeeeeeee-eeee-eeee-eeee-000000000008', 'bbbbbbbb-bbbb-bbbb-bbbb-000000000001', 'dddddddd-dddd-dddd-dddd-000000000008', 8)
ON CONFLICT (id) DO NOTHING;
