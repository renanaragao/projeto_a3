package com.projeto.gestao.controller;

import com.projeto.gestao.model.Tarefa;
import com.projeto.gestao.model.Projeto;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.StatusTarefa;
import com.projeto.gestao.repository.TarefaRepository;
import com.projeto.gestao.repository.ProjetoRepository;
import com.projeto.gestao.repository.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TarefasController extends BaseController {
    @FXML private TableView<Tarefa> tblTarefas;
    @FXML private TableColumn<Tarefa, String> colTitulo;
    @FXML private TableColumn<Tarefa, String> colDescricao;
    @FXML private TableColumn<Tarefa, Projeto> colProjeto;
    @FXML private TableColumn<Tarefa, Usuario> colResponsavel;
    @FXML private TableColumn<Tarefa, StatusTarefa> colStatus;
    @FXML private TableColumn<Tarefa, LocalDate> colDataInicio;
    @FXML private TableColumn<Tarefa, LocalDate> colDataFim;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDescricao;
    @FXML private ComboBox<Projeto> cbProjeto;
    @FXML private ComboBox<Usuario> cbResponsavel;
    @FXML private ComboBox<StatusTarefa> cbStatus;
    @FXML private DatePicker dpDataInicio;
    @FXML private DatePicker dpDataFim;
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;
    @FXML private Button btnCancelar;

    @FXML private TextField filtroTitulo;
    @FXML private ComboBox<Projeto> filtroProjeto;
    @FXML private ComboBox<Usuario> filtroResponsavel;
    @FXML private ComboBox<StatusTarefa> filtroStatus;
    @FXML private Button btnFiltrar;
    @FXML private Button btnLimparFiltro;

    private final TarefaRepository tarefaRepository = new TarefaRepository();
    private final ProjetoRepository projetoRepository = new ProjetoRepository();
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final ObservableList<Tarefa> tarefas = FXCollections.observableArrayList();
    private final ObservableList<Projeto> projetos = FXCollections.observableArrayList();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private Tarefa tarefaSelecionada;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProjetos();
        carregarUsuarios();
        carregarTarefas();
        cbStatus.setItems(FXCollections.observableArrayList(StatusTarefa.values()));
        filtroStatus.setItems(FXCollections.observableArrayList(StatusTarefa.values()));
        configurarComboBoxProjeto(cbProjeto);
        configurarComboBoxProjeto(filtroProjeto);
        configurarComboBoxUsuario(cbResponsavel);
        configurarComboBoxUsuario(filtroResponsavel);
        configurarComboBoxStatus(cbStatus);
        configurarComboBoxStatus(filtroStatus);
        configurarAcoes();
        configurarSelecaoTabela();
        estadoInicial();
    }

    private void configurarTabela() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colProjeto.setCellValueFactory(new PropertyValueFactory<>("projeto"));
        colResponsavel.setCellValueFactory(new PropertyValueFactory<>("responsavel"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicioPrevista"));
        colDataFim.setCellValueFactory(new PropertyValueFactory<>("dataFimPrevista"));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colDataInicio.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
            }
        });
        colDataFim.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : df.format(item));
            }
        });
        colProjeto.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        colResponsavel.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNomeCompleto());
            }
        });
        colStatus.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(StatusTarefa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : statusLabel(item));
            }
        });
        tblTarefas.setItems(tarefas);
    }

    private void carregarProjetos() {
        projetos.setAll(projetoRepository.listarTodos());
        cbProjeto.setItems(projetos);
        if (filtroProjeto != null) filtroProjeto.setItems(projetos);
    }

    private void carregarUsuarios() {
        usuarios.setAll(usuarioRepository.listarTodos());
        cbResponsavel.setItems(usuarios);
        if (filtroResponsavel != null) filtroResponsavel.setItems(usuarios);
    }

    private void configurarComboBoxProjeto(ComboBox<Projeto> comboBox) {
        if (comboBox == null) return;
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Projeto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNome());
            }
        });
    }

    private void configurarComboBoxUsuario(ComboBox<Usuario> comboBox) {
        if (comboBox == null) return;
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNomeCompleto());
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getNomeCompleto());
            }
        });
    }

    private void configurarComboBoxStatus(ComboBox<StatusTarefa> comboBox) {
        comboBox.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(StatusTarefa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : statusLabel(item));
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override protected void updateItem(StatusTarefa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : statusLabel(item));
            }
        });
    }

    private String statusLabel(StatusTarefa st) {
        return switch (st) {
            case TODO -> "Pendente";
            case IN_PROGRESS -> "Em Execução";
            case TEST -> "Teste";
            case DONE -> "Concluída";
            case CANCELADA -> "Cancelada";
        };
    }

    private void configurarAcoes() {
        btnFiltrar.setOnAction(e -> filtrarTarefas());
        btnLimparFiltro.setOnAction(e -> limparFiltros());
        btnNovo.setOnAction(e -> novo());
        btnCancelar.setOnAction(e -> cancelar());
        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
    }

    private void configurarSelecaoTabela() {
        tblTarefas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            tarefaSelecionada = newV;
            if (newV != null) preencherFormulario(newV);
            alterarEstadoEdicao(newV != null);
        });
    }

    private void estadoInicial() {
        limparFormulario();
        alterarEstadoEdicao(false);
    }

    private void alterarEstadoEdicao(boolean editando) {
        btnExcluir.setDisable(!editando);
    }

    private void novo() {
        tarefaSelecionada = null;
        limparFormulario();
        alterarEstadoEdicao(true);
    }

    private void cancelar() {
        tarefaSelecionada = null;
        limparFormulario();
        alterarEstadoEdicao(false);
        tblTarefas.getSelectionModel().clearSelection();
    }

    private void preencherFormulario(Tarefa t) {
        txtTitulo.setText(t.getTitulo());
        txtDescricao.setText(t.getDescricao());
        cbProjeto.setValue(t.getProjeto());
        cbResponsavel.setValue(t.getResponsavel());
        cbStatus.setValue(t.getStatus());
        dpDataInicio.setValue(t.getDataInicioPrevista());
        dpDataFim.setValue(t.getDataFimPrevista());
    }

    private void limparFormulario() {
        txtTitulo.clear();
        txtDescricao.clear();
        cbProjeto.setValue(null);
        cbResponsavel.setValue(null);
        cbStatus.setValue(null);
        dpDataInicio.setValue(null);
        dpDataFim.setValue(null);
    }

    private void salvar() {
        if (!validar()) return;
        Tarefa t = tarefaSelecionada == null ? new Tarefa() : tarefaSelecionada;
        t.setTitulo(txtTitulo.getText().trim());
        t.setDescricao(txtDescricao.getText().trim());
        t.setProjeto(cbProjeto.getValue());
        t.setResponsavel(cbResponsavel.getValue());
        t.setStatus(cbStatus.getValue() == null ? StatusTarefa.TODO : cbStatus.getValue());
        t.setDataInicioPrevista(dpDataInicio.getValue());
        t.setDataFimPrevista(dpDataFim.getValue());
        tarefaRepository.salvar(t);
        if (tarefaSelecionada == null) tarefas.add(0, t); // adiciona no topo
        tblTarefas.refresh();
        cancelar();
    }

    private boolean validar() {
        StringBuilder sb = new StringBuilder();
        if (txtTitulo.getText() == null || txtTitulo.getText().isBlank()) sb.append("Título é obrigatório.\n");
        if (cbProjeto.getValue() == null) sb.append("Projeto é obrigatório.\n");
        if (sb.length() > 0) {
            new Alert(Alert.AlertType.WARNING, sb.toString(), ButtonType.OK).showAndWait();
            return false;
        }
        return true;
    }

    private void excluir() {
        if (tarefaSelecionada == null) return;
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, "Confirmar cancelamento da tarefa?", ButtonType.YES, ButtonType.NO);
        a.setHeaderText(null);
        a.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                tarefaRepository.excluir(tarefaSelecionada.getId());
                tarefas.remove(tarefaSelecionada);
                cancelar();
            }
        });
    }

    private void carregarTarefas() {
        tarefas.setAll(tarefaRepository.listarTodas());
    }

    private void filtrarTarefas() {
        String titulo = filtroTitulo.getText() != null ? filtroTitulo.getText().trim().toLowerCase() : "";
        Projeto proj = filtroProjeto.getValue();
        Usuario resp = filtroResponsavel.getValue();
        StatusTarefa st = filtroStatus.getValue();
        tarefas.setAll(tarefaRepository.listarTodas().stream()
                .filter(t -> titulo.isEmpty() || (t.getTitulo() != null && t.getTitulo().toLowerCase().contains(titulo)))
                .filter(t -> proj == null || t.getProjeto() != null && t.getProjeto().getId().equals(proj.getId()))
                .filter(t -> resp == null || (t.getResponsavel() != null && t.getResponsavel().getId().equals(resp.getId())))
                .filter(t -> st == null || t.getStatus() == st)
                .toList());
    }

    private void limparFiltros() {
        filtroTitulo.clear();
        filtroProjeto.setValue(null);
        filtroResponsavel.setValue(null);
        filtroStatus.setValue(null);
        carregarTarefas();
    }

    @Override
    protected void configurarTela() {
        // Implementar se necessário para permissões
    }
}
