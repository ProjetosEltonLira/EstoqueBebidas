--Arquivo usado para executar comandos SQL e  inserir valores de forma antecipada nas tabelas,

INSERT INTO tb_tipo_bebida (tipo_bebida_id, descricao)
    VALUES (1,'ALCOOLICA'),
           (2,'SEM ALCOOL'),
           (3,'REFRIGERANTE'),
           (4,'SUCO'),
           (5,'CERVEJA') ,
           (6,'VINHO'),
           (7,'AGUA')
    ON CONFLICT (tipo_bebida_id) DO NOTHING;

-- tb_products
--INSERT INTO tb_products (product_id, product_name, price)
--    VALUES
--        (1, 'Computer', 4500.50),
--        (2, 'Smartphone', 2000.00),d
--        (3, 'Mouse', 200.00)
--    ON CONFLICT (product_id) DO NOTHING;


-- tb_tags
-- tb_products_tags
--INSERT INTO tb_products_tags (product_id, tag_id)
--    VALUES
--        (1, 1),
--        (2, 3),
--        (2, 1),
--        (3, 1)
--    ON CONFLICT (product_id, tag_id) DO NOTHING;