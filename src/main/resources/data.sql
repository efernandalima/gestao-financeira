-- Inserir categorias de RECEITA
INSERT INTO categorias (nome, descricao, tipo) VALUES
                                                   ('Salário', 'Salário mensal', 'RECEITA'),
                                                   ('Freelance', 'Trabalhos freelance', 'RECEITA'),
                                                   ('Investimentos', 'Rendimentos de investimentos', 'RECEITA'),
                                                   ('Outros Ganhos', 'Outras receitas', 'RECEITA');

-- Inserir categorias de DESPESA
INSERT INTO categorias (nome, descricao, tipo) VALUES
                                                   ('Alimentação', 'Mercado e restaurantes', 'DESPESA'),
                                                   ('Transporte', 'Combustível e transporte público', 'DESPESA'),
                                                   ('Moradia', 'Aluguel e contas da casa', 'DESPESA'),
                                                   ('Saúde', 'Medicamentos e consultas', 'DESPESA'),
                                                   ('Lazer', 'Entretenimento e viagens', 'DESPESA'),
                                                   ('Educação', 'Cursos e livros', 'DESPESA');

-- Exemplos de transações
INSERT INTO transacoes (descricao, valor, data, tipo, categoria_id, observacao) VALUES
                                                                                    ('Salário Janeiro', 5000.00, '2026-01-05', 'RECEITA', 1, 'Salário do mês'),
                                                                                    ('Freelance Site', 1500.00, '2026-01-10', 'RECEITA', 2, 'Desenvolvimento de site'),
                                                                                    ('Mercado Atacadão', 450.00, '2026-01-12', 'DESPESA', 5, 'Compras do mês'),
                                                                                    ('Gasolina', 250.00, '2026-01-15', 'DESPESA', 6, 'Abastecimento'),
                                                                                    ('Aluguel', 1200.00, '2026-01-10', 'DESPESA', 7, 'Aluguel Janeiro');
