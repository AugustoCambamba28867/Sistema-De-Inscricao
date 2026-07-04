package sdpfrequencia;

/*------------------------------------
Tema: Sistema de Inscrição e Gestão de Fichas (GET Training)
Grupo: 3
Ficheiro: Analise.java
Data: 04.05.2026
--------------------------------------*

/*
1. Objectivo
Este projecto tem o objectivo de registar, armazenar e listar as fichas de inscrição 
dos formandos da GET Training Academy Center, separando rigorosamente a interface 
visual das regras de negócio e da manipulação do ficheiro binário.

2. Visao [Interfaces Graficas - Padrão SPA]
- Launcher (Content Manager / Dashboard)
- InscricaoPanel (Módulo de Cadastro Refinado)
- RelatoriosPanel (Tabela Profissional com Filtro em Tempo Real)
- AdministracaoPanel (Estatísticas e Manutenção)

3. Entidades Fortes (Modelo)
- FichaInscricao (Abstração completa dos dados do formando)

4. Persistência
- dados.dat (RandomAccessFile - Binário)

5. Tabelas de Apoio
- Geridas via enumeradores lógicos e componentes UI (Horários/Sexo)

6. Pesquisas e Filtros
- Implementação de FilteredList na TableView para busca instantânea.

7. Diversos
7.1 - Arquitetura: Single Page Application (SPA) Desktop
7.2 - Tecnologias: Java 8 (JavaFX Puro)
7.3 - Design: Elite UI (CSS Inline / Material Design inspirado)
*/

public class Analise {
	// Este ficheiro serve exclusivamente como base de documentação arquitetural e
	// análise do projeto para o Grupo 3.
}