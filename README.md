# projeto_a3

## Tecnologias envolvidas
O Projeto deve ser desenvolvido utilizando-se a linguagem Java com interface visual (Swing, JavaFX ou similar), conectado a um banco de dados MySQL. O sistema deverá ter diferentes perfis de acesso (ex: administrador, gerente de projeto, colaborador) e permitir requisitos explícitos e implícitos.

## Requisitos Iniciais (explícitos)

1. **Cadastro de Usuários**
   1. Nome completo, CPF, e-mail, cargo, login, senha.
   2. Cada usuário deve ter um perfil: administrador, gerente ou colaborador.

2. **Cadastro de Projetos**
   1. Nome do projeto, descrição, data de início, data de término prevista, status (planejado, em andamento, concluído, cancelado).
   2. Cada projeto deve ter um gerente responsável.

3. **Cadastro de Equipes**
   1. Nome da equipe, descrição, membros (usuários vinculados).
   2. Uma equipe pode atuar em vários projetos.

4. **Alocação de Equipes a Projetos**
   1. Um projeto pode ter mais de uma equipe envolvida.
   2. Uma equipe pode atuar em mais de um projeto.

5. **Cadastro de Tarefas**
   1. Título, descrição, projeto vinculado, responsável (usuário), status (pendente, em execução, concluída), data de início e fim previstas e reais.
   2. Cada tarefa pertence a um único projeto.

6. **Relatórios e Dashboards**
   1. Resumo de andamento dos projetos.
   2. Desempenho de cada colaborador (tarefas atribuídas e concluídas).
   3. Projetos com risco de atraso (data atual > data prevista de término).

7. **Autenticação**
   1. Tela de login com validação no banco de dados.

8. **Interface visual amigável**
   1. Prototipar telas antes de codificar.
   2. Interface deve permitir fácil navegação, cadastro, edição e visualização dos dados.

## Requisitos Implícitos (o aluno deve pensar sobre)
1. Como será feita a vinculação entre tarefas, projetos e colaboradores?
2. É possível que um colaborador esteja em mais de uma equipe?
3. O que acontece com as tarefas se o projeto for cancelado?
4. Será necessário histórico de alterações nos status das tarefas?
5. Como controlar quem pode alterar o quê, com base no perfil do usuário?
6. De que forma será feito o relacionamento entre tabelas no banco?
7. Qual a melhor forma de organizar os pacotes da aplicação Java?
8. Haverá necessidade de logs de acesso ou atividades do sistema?
9. Qual o tratamento ideal para campos obrigatórios e validações de formulário?
