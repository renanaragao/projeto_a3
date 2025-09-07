# Documento de Especificação de Requisitos de Software (ERS)

## 1. Introdução
Este documento apresenta a especificação de requisitos de software do sistema de gerenciamento de projetos, que será desenvolvido em **Java** com interface gráfica (Swing, JavaFX ou similar) e banco de dados **MySQL**.  
O objetivo é garantir clareza nas funcionalidades, restrições e expectativas, fornecendo base sólida para desenvolvimento, testes e manutenção.

---

## 2. Tecnologias Envolvidas
- Linguagem: **Java**  
- Interface gráfica: **Swing**, **JavaFX** ou similar  
- Banco de Dados: **MySQL**  
- Arquitetura: **MVC (Model-View-Controller)**  
- Perfis de acesso:
  - **Administrador**
  - **Gerente de Projeto**
  - **Colaborador**

---

## 3. Requisitos Funcionais

### 3.1 Cadastro de Usuários
- O sistema deve permitir o cadastro de usuários com os seguintes dados:
  - Nome completo
  - CPF
  - E-mail
  - Cargo
  - Login
  - Senha
- Cada usuário deve possuir um perfil: **administrador, gerente ou colaborador**.

### 3.2 Cadastro de Projetos
- O sistema deve permitir cadastrar projetos com:
  - Nome
  - Descrição
  - Data de início
  - Data de término prevista
  - Status: *planejado, em andamento, concluído, cancelado*
- Cada projeto deve ter um **gerente responsável**.

### 3.3 Cadastro de Equipes
- O sistema deve permitir cadastrar equipes com:
  - Nome
  - Descrição
  - Membros (usuários vinculados)
- Uma equipe pode participar de **vários projetos**.

### 3.4 Alocação de Equipes a Projetos
- Um projeto pode ter **mais de uma equipe envolvida**.
- Uma equipe pode atuar em **mais de um projeto**.

### 3.5 Cadastro de Tarefas
- O sistema deve permitir cadastrar tarefas com:
  - Título
  - Descrição
  - Projeto vinculado
  - Responsável (usuário)
  - Status: *pendente, em execução, concluída*
  - Datas de início e fim previstas
  - Datas de início e fim reais
- Cada tarefa deve pertencer a **um único projeto**.

### 3.6 Relatórios e Dashboards
- Resumo de andamento dos projetos.  
- Desempenho de cada colaborador (tarefas atribuídas e concluídas).  
- Listagem de projetos com **risco de atraso** (data atual > data prevista de término).

### 3.7 Autenticação
- O sistema deve ter tela de login com validação no banco de dados.  
- O acesso deve ser controlado por **perfil de usuário**.

### 3.8 Interface Visual
- Telas devem ser **prototipadas antes da codificação**.  
- Interface deve ser **intuitiva**, permitindo:
  - Navegação simples  
  - Cadastro de informações  
  - Edição e visualização de dados  

---

## 4. Requisitos Não Funcionais
- **Desempenho:** sistema deve responder em tempo aceitável (< 2s em operações comuns).  
- **Segurança:** senhas armazenadas com criptografia.  
- **Usabilidade:** interface amigável e consistente.  
- **Confiabilidade:** não deve ocorrer perda de dados em transações.  
- **Escalabilidade:** banco preparado para crescimento de número de usuários, projetos e tarefas.  

---

## 5. Requisitos Implícitos
- Vinculação entre tarefas, projetos e colaboradores será feita via **relacionamentos no banco** (chaves estrangeiras).  
- Um colaborador pode participar de **mais de uma equipe**.  
- Se um projeto for cancelado, suas tarefas devem ser marcadas como **canceladas**.  
- Histórico de alterações de status das tarefas deve ser mantido para auditoria.  
- Controle de permissões baseado em **perfil de usuário**:
  - Administrador: acesso total.  
  - Gerente: pode criar e gerenciar projetos, equipes e tarefas.  
  - Colaborador: apenas visualizar e atualizar tarefas atribuídas.  
- Relacionamentos no banco de dados devem seguir **modelo relacional normalizado**.  
- Pacotes da aplicação Java devem ser organizados por camadas:
  - `model`
  - `repository`
  - `service`
  - `controller`
  - `view`  
- Logs de acesso e atividades relevantes devem ser armazenados.  
- Campos obrigatórios devem ter **validação de formulário**.  

---

## 6. Considerações Finais
Este documento define a base dos requisitos funcionais e não funcionais do sistema, servindo como guia para o desenvolvimento, testes e futura manutenção.  
Quaisquer mudanças deverão ser registradas e revisadas em versões futuras desta especificação.
