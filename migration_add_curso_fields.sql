-- ============================================================
-- MIGRAÇÃO: Adicionar campos à tabela 'curso'
-- Data: 2026-07-08
-- ============================================================
-- IMPORTANTE: Execute este script na base de dados existente
-- para adicionar os novos campos de período e horas.

-- Verificar se a coluna periodo existe (caso contrário, adicionar)
ALTER TABLE curso ADD COLUMN IF NOT EXISTS periodo DATE;
ALTER TABLE curso ADD COLUMN IF NOT EXISTS hora_inicio VARCHAR(5);
ALTER TABLE curso ADD COLUMN IF NOT EXISTS hora_fim VARCHAR(5);
ALTER TABLE curso ADD COLUMN IF NOT EXISTS duracao VARCHAR(50);

-- Remover a coluna 'horario' (comentada - descomente se quiser remover após validação)
-- ALTER TABLE curso DROP COLUMN IF EXISTS horario;

-- Verificar a estrutura da tabela após alteração
\d curso

-- ============================================================
-- FIM DA MIGRAÇÃO
-- ============================================================
