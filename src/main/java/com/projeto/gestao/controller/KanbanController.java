package com.projeto.gestao.controller;

import com.projeto.gestao.model.*;
import com.projeto.gestao.service.TarefaService;
import com.projeto.gestao.service.ProjetoService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import java.util.List;

public class KanbanController extends BaseController {
    @FXML private ComboBox<Projeto> cbProjeto;
    @FXML private VBox colunaTodo;
    @FXML private VBox colunaInProgress;
    @FXML private VBox colunaTest;
    @FXML private VBox colunaDone;
    @FXML private Label lblTodo;
    @FXML private Label lblInProgress;
    @FXML private Label lblTest;
    @FXML private Label lblDone;

    private TarefaService tarefaService = new TarefaService();
    private ProjetoService projetoService = new ProjetoService();
    private static final DataFormat TAREFA_FORMAT = new DataFormat("tarefa");

    @FXML
    private void initialize() {
        configurarDragAndDrop();
        cbProjeto.setOnAction(e -> carregarTarefas());
    }

    @Override
    protected void configurarTela() {
        carregarProjetos();
    }

    private void carregarProjetos() {
        List<Projeto> projetos;
        if (usuarioLogado.getPerfil() == PerfilUsuario.ADMINISTRADOR) {
            projetos = projetoService.listarTodos();
        } else {
            projetos = projetoService.listarPorGerente(usuarioLogado);
        }
        cbProjeto.setItems(FXCollections.observableArrayList(projetos));
        if (!projetos.isEmpty()) {
            cbProjeto.getSelectionModel().selectFirst();
            carregarTarefas();
        }
    }

    private void carregarTarefas() {
        Projeto projetoSelecionado = cbProjeto.getSelectionModel().getSelectedItem();
        if (projetoSelecionado == null) return;

        limparColunas();

        List<Tarefa> tarefasTodo = tarefaService.listarPorProjetoEStatus(projetoSelecionado, StatusTarefa.TODO);
        List<Tarefa> tarefasInProgress = tarefaService.listarPorProjetoEStatus(projetoSelecionado, StatusTarefa.IN_PROGRESS);
        List<Tarefa> tarefasTest = tarefaService.listarPorProjetoEStatus(projetoSelecionado, StatusTarefa.TEST);
        List<Tarefa> tarefasDone = tarefaService.listarPorProjetoEStatus(projetoSelecionado, StatusTarefa.DONE);

        adicionarTarefasNaColuna(colunaTodo, tarefasTodo);
        adicionarTarefasNaColuna(colunaInProgress, tarefasInProgress);
        adicionarTarefasNaColuna(colunaTest, tarefasTest);
        adicionarTarefasNaColuna(colunaDone, tarefasDone);

        atualizarContadores();
    }

    private void limparColunas() {
        colunaTodo.getChildren().removeIf(node -> !(node instanceof Label));
        colunaInProgress.getChildren().removeIf(node -> !(node instanceof Label));
        colunaTest.getChildren().removeIf(node -> !(node instanceof Label));
        colunaDone.getChildren().removeIf(node -> !(node instanceof Label));
    }

    private void adicionarTarefasNaColuna(VBox coluna, List<Tarefa> tarefas) {
        for (Tarefa tarefa : tarefas) {
            Button btnTarefa = criarBotaoTarefa(tarefa);
            coluna.getChildren().add(btnTarefa);
        }
    }

    private Button criarBotaoTarefa(Tarefa tarefa) {
        Button btn = new Button(tarefa.getTitulo());
        btn.getStyleClass().add("tarefa-kanban");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setWrapText(true);
        btn.setUserData(tarefa);

        String prioridadeClass = switch (tarefa.getPrioridade()) {
            case ALTA -> "prioridade-alta";
            case CRITICA -> "prioridade-critica";
            case BAIXA -> "prioridade-baixa";
            default -> "prioridade-media";
        };
        btn.getStyleClass().add(prioridadeClass);

        configurarDragTarefa(btn);

        btn.setOnAction(e -> mostrarDetalhesTarefa(tarefa));

        return btn;
    }

    private void configurarDragTarefa(Button btnTarefa) {
        btnTarefa.setOnDragDetected(event -> {
            Dragboard db = btnTarefa.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.put(TAREFA_FORMAT, btnTarefa.getUserData());
            db.setContent(content);
            event.consume();
        });
    }

    private void configurarDragAndDrop() {
        configurarDropColuna(colunaTodo, StatusTarefa.TODO);
        configurarDropColuna(colunaInProgress, StatusTarefa.IN_PROGRESS);
        configurarDropColuna(colunaTest, StatusTarefa.TEST);
        configurarDropColuna(colunaDone, StatusTarefa.DONE);
    }

    private void configurarDropColuna(VBox coluna, StatusTarefa status) {
        coluna.setOnDragOver(event -> {
            if (event.getGestureSource() != coluna && event.getDragboard().hasContent(TAREFA_FORMAT)) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        coluna.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(TAREFA_FORMAT)) {
                Tarefa tarefa = (Tarefa) db.getContent(TAREFA_FORMAT);

                try {
                    int novaPosicao = coluna.getChildren().size() - 1;
                    tarefaService.moverTarefaKanban(tarefa.getId(), status, novaPosicao);
                    carregarTarefas();
                    success = true;

                    mostrarMensagemSucesso("Tarefa movida com sucesso!");

                } catch (Exception e) {
                    mostrarMensagemErro("Erro ao mover tarefa: " + e.getMessage());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void atualizarContadores() {
        int todoCount = colunaTodo.getChildren().size() - 1;
        int inProgressCount = colunaInProgress.getChildren().size() - 1;
        int testCount = colunaTest.getChildren().size() - 1;
        int doneCount = colunaDone.getChildren().size() - 1;

        lblTodo.setText("To Do (" + todoCount + ")");
        lblInProgress.setText("In Progress (" + inProgressCount + ")");
        lblTest.setText("Test (" + testCount + ")");
        lblDone.setText("Done (" + doneCount + ")");
    }

    private void mostrarDetalhesTarefa(Tarefa tarefa) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes da Tarefa");
        alert.setHeaderText(tarefa.getTitulo());

        StringBuilder detalhes = new StringBuilder();
        detalhes.append("Descrição: ").append(tarefa.getDescricao() != null ? tarefa.getDescricao() : "N/A").append("\n");
        detalhes.append("Responsável: ").append(tarefa.getResponsavel() != null ? tarefa.getResponsavel().getNomeCompleto() : "N/A").append("\n");
        detalhes.append("Prioridade: ").append(tarefa.getPrioridade()).append("\n");
        detalhes.append("Status: ").append(tarefa.getStatus()).append("\n");

        if (tarefa.getDataInicioPrevista() != null) {
            detalhes.append("Início Previsto: ").append(tarefa.getDataInicioPrevista()).append("\n");
        }
        if (tarefa.getDataFimPrevista() != null) {
            detalhes.append("Fim Previsto: ").append(tarefa.getDataFimPrevista()).append("\n");
        }

        alert.setContentText(detalhes.toString());
        alert.showAndWait();
    }

    private void mostrarMensagemSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarMensagemErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
