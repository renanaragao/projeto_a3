# Sistema de Gestão de Projetos

Sistema completo de gerenciamento de projetos desenvolvido em Java com JavaFX, Hibernate e MySQL, seguindo arquitetura MVC com funcionalidades avançadas de Kanban Board interativo.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **JavaFX 21** - Interface gráfica moderna e responsiva
- **Hibernate 6.1.7** - ORM para persistência de dados
- **MySQL 8.0** - Banco de dados relacional
- **BCrypt** - Criptografia de senhas
- **Maven** - Gerenciamento de dependências

## 📋 Funcionalidades

### ✅ Gestão de Usuários
- Cadastro, edição e exclusão de usuários
- Três perfis de acesso: Administrador, Gerente e Colaborador
- Autenticação segura com senhas criptografadas
- Controle de permissões por perfil

### ✅ Gestão de Projetos
- CRUD completo de projetos
- Associação com gerentes responsáveis
- Controle de status (Planejado, Em Andamento, Concluído, Cancelado)
- Alertas para projetos com risco de atraso

### ✅ Gestão de Equipes
- Criação e gerenciamento de equipes
- Vinculação de usuários às equipes
- Alocação de equipes aos projetos
- Relacionamento N:N entre equipes e projetos

### ✅ Gestão de Tarefas
- CRUD completo de tarefas
- Atribuição de responsáveis
- Controle de datas previstas e reais
- Sistema de prioridades

### ✅ Kanban Board Interativo
- Interface visual estilo Kanban
- Colunas: To Do, In Progress, Test, Done
- **Drag-and-Drop** funcional entre colunas
- Atualização automática no banco de dados
- Indicadores visuais de prioridade
- Contadores de tarefas por coluna

### ✅ Relatórios e Dashboards
- Dashboard geral do sistema
- Relatórios de desempenho por colaborador
- Estatísticas de projetos e tarefas
- Identificação de projetos em risco

### ✅ Auditoria e Logs
- Registro automático de todas as atividades
- Logs de login/logout dos usuários
- Histórico de alterações nas entidades

## 🛠️ Configuração do Ambiente

### Pré-requisitos

1. **Java 17** ou superior
2. **MySQL 8.0** ou superior
3. **Maven 3.6** ou superior

### Configuração do Banco de Dados

1. Instale e inicie o MySQL
2. Configure as credenciais conforme especificado:
   - **Host:** 127.0.0.1
   - **Porta:** 3306
   - **Usuário:** root
   - **Senha:** gtk2green

3. O banco `gestao_projetos` será criado automaticamente na primeira execução

### Instalação e Execução

1. **Clone ou baixe o projeto:**
```bash
cd projeto_a3
```

2. **Instale as dependências:**
```bash
mvn clean install
```

3. **Execute a aplicação:**
```bash
mvn javafx:run
```

## 👥 Usuários Padrão

O sistema cria automaticamente três usuários para teste:

| Perfil | Login | Senha | Descrição |
|--------|-------|-------|-----------|
| Administrador | `admin` | `admin123` | Acesso total ao sistema |
| Gerente | `gerente` | `gerente123` | Gestão de projetos e equipes |
| Colaborador | `colaborador` | `colaborador123` | Visualização e atualização de tarefas |

## 🎯 Como Usar

### 1. Login no Sistema
- Inicie a aplicação
- Use uma das credenciais padrão para fazer login
- O sistema redirecionará para a tela principal

### 2. Navegação
- **Menu lateral** com acesso às funcionalidades
- **Permissões automáticas** baseadas no perfil do usuário
- **Interface responsiva** que se adapta ao tamanho da tela

### 3. Kanban Board
- Acesse "Kanban Board" no menu
- Selecione um projeto no dropdown
- **Arraste e solte** tarefas entre as colunas
- As mudanças são salvas automaticamente no banco

### 4. Gestão de Dados
- **Usuários:** Cadastre novos usuários com diferentes perfis
- **Projetos:** Crie projetos e associe gerentes
- **Equipes:** Monte equipes e aloque aos projetos
- **Tarefas:** Crie tarefas e atribua responsáveis

### 5. Relatórios
- Acesse dashboards com métricas do sistema
- Visualize desempenho dos colaboradores
- Identifique projetos em risco de atraso

## 🏗️ Arquitetura

O sistema segue o padrão **MVC (Model-View-Controller)**:

```
src/main/java/com/projeto/gestao/
├── model/           # Entidades JPA
├── repository/      # Camada de acesso a dados
├── service/         # Lógica de negócio
├── controller/      # Controladores JavaFX
├── view/           # (Arquivos FXML)
└── util/           # Utilitários e configurações
```

### Entidades Principais
- **Usuario** - Usuários do sistema com perfis
- **Projeto** - Projetos com status e gerentes
- **Equipe** - Equipes de trabalho
- **Tarefa** - Tarefas com status Kanban
- **LogAtividade** - Auditoria de atividades

## 🔧 Configurações Avançadas

### Modificar Configurações do Banco
Edite o arquivo `src/main/resources/hibernate.cfg.xml`:

```xml
<property name="hibernate.connection.url">jdbc:mysql://SEU_HOST:PORTA/gestao_projetos</property>
<property name="hibernate.connection.username">SEU_USUARIO</property>
<property name="hibernate.connection.password">SUA_SENHA</property>
```

### Personalizar Interface
Modifique o arquivo `src/main/resources/css/style.css` para alterar cores e estilos.

## 🚨 Solução de Problemas

### Erro de Conexão com MySQL
- Verifique se o MySQL está rodando
- Confirme as credenciais no arquivo `hibernate.cfg.xml`
- Verifique se a porta 3306 está disponível

### Erro de JavaFX
- Certifique-se de estar usando Java 17
- Execute com: `mvn javafx:run`

### Erro de Dependências
- Execute: `mvn clean install`
- Verifique sua conexão com a internet

## 📊 Funcionalidades do Kanban

### Drag-and-Drop
- **Arraste** tarefas entre colunas To Do → In Progress → Test → Done
- **Solte** na coluna desejada para mover a tarefa
- **Atualização automática** do status no banco de dados

### Indicadores Visuais
- **Bordas coloridas** indicam prioridade:
  - 🟢 Verde: Baixa
  - 🟡 Amarelo: Média  
  - 🟠 Laranja: Alta
  - 🔴 Vermelho: Crítica

### Informações das Tarefas
- **Clique** em uma tarefa para ver detalhes completos
- **Contadores** mostram quantidade de tarefas por coluna
- **Filtro por projeto** no dropdown superior

## 🔐 Segurança

- **Senhas criptografadas** com BCrypt
- **Controle de sessão** baseado em perfis
- **Logs de auditoria** para todas as operações
- **Validação de formulários** em tempo real

## 📈 Relatórios Disponíveis

1. **Dashboard Geral**
   - Total de projetos, tarefas e usuários
   - Projetos ativos e com risco
   - Percentual de tarefas concluídas

2. **Desempenho por Colaborador**
   - Tarefas atribuídas vs concluídas
   - Percentual de conclusão
   - Tarefas em andamento

3. **Status dos Projetos**
   - Progresso por projeto
   - Distribuição por status
   - Projetos em atraso

## 🎨 Interface

- **Design moderno** com cores profissionais
- **Responsiva** e intuitiva
- **Ícones visuais** para facilitar navegação
- **Feedback visual** em todas as operações

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 🤝 Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

---

**Sistema desenvolvido seguindo as especificações do ERS e implementando todas as funcionalidades solicitadas, incluindo o Kanban Board interativo com drag-and-drop.**
