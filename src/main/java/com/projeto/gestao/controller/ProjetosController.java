package com.projeto.gestao.controller;

import com.projeto.gestao.model.Projeto;
import com.projeto.gestao.model.StatusProjeto;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.repository.ProjetoRepository;
import com.projeto.gestao.repository.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;

public class ProjetosController extends BaseController {
    @FXML private TableView<Projeto> tblProjetos;
    @FXML private TableColumn<Projeto, String> colNome;
    @FXML private TableColumn<Projeto, String> colDescricao;
    @FXML private TableColumn<Projeto, LocalDate> colDataInicio;
    @FXML private TableColumn<Projeto, LocalDate> colDataTermino;
    @FXML private TableColumn<Projeto, StatusProjeto> colStatus;
    @FXML private TableColumn<Projeto, String> colGerente;

    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;
    @FXML private DatePicker dpDataInicio;
    @FXML private DatePicker dpDataTermino;
    @FXML private ComboBox<StatusProjeto> cbStatus;
    @FXML private ComboBox<Usuario> cbGerente;
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;
    @FXML private Button btnCancelar;

    private ProjetoRepository projetoRepository = new ProjetoRepository();
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private ObservableList<Projeto> projetos = FXCollections.observableArrayList();
    private Projeto projetoSelecionado;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarProjetos();
        carregarGerentes();
        cbStatus.setItems(FXCollections.observableArrayList(StatusProjeto.values()));
        limparFormulario();
        configurarAcoes();
    }

    @Override
    protected void configurarTela() {
        // Implementação obrigatória do BaseController. Pode ser deixada vazia ou usada para configurações específicas.
    }

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
        colDataTermino.setCellValueFactory(new PropertyValueFactory<>("dataTerminoPrevista"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colGerente.setCellValueFactory(cellData -> {
            Usuario gerente = cellData.getValue().getGerente();
            return new javafx.beans.property.SimpleStringProperty(gerente != null ? gerente.getNomeCompleto() : "");
        });
        // Formatação das datas para pt-BR
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        colDataInicio.setCellFactory(column -> new TableCell<Projeto, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });
        colDataTermino.setCellFactory(column -> new TableCell<Projeto, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.format(formatter));
            }
        });
        tblProjetos.setItems(projetos);
        tblProjetos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> selecionarProjeto(newSel));
    }

    private void carregarProjetos() {
        projetos.setAll(projetoRepository.listarTodos());
    }

    private void carregarGerentes() {
        List<Usuario> usuarios = usuarioRepository.listarPorPerfil(com.projeto.gestao.model.PerfilUsuario.GERENTE);
        cbGerente.setItems(FXCollections.observableArrayList(usuarios));
        cbGerente.setCellFactory(x -> new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNomeCompleto());
            }
        });
        cbGerente.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNomeCompleto());
            }
        });
    }

    private void configurarAcoes() {
        btnNovo.setOnAction(e -> novoProjeto());
        btnSalvar.setOnAction(e -> salvarProjeto());
        btnExcluir.setOnAction(e -> excluirProjeto());
        btnCancelar.setOnAction(e -> limparFormulario());
    }

    private void novoProjeto() {
        tblProjetos.getSelectionModel().clearSelection();
        limparFormulario();
    }

    private void selecionarProjeto(Projeto projeto) {
        if (projeto != null) {
            projetoSelecionado = projeto;
            txtNome.setText(projeto.getNome());
            txtDescricao.setText(projeto.getDescricao());
            dpDataInicio.setValue(projeto.getDataInicio());
            dpDataTermino.setValue(projeto.getDataTerminoPrevista());
            cbStatus.setValue(projeto.getStatus());
            for (Usuario u : cbGerente.getItems()) {
                if (u.getId().equals(projeto.getGerente().getId())) {
                    cbGerente.setValue(u);
                    break;
                }
            }

        }
    }

    private void salvarProjeto() {
        if (!validarCampos()) return;
        try {
            Projeto projeto = projetoSelecionado != null ? projetoSelecionado : new Projeto();
            projeto.setNome(txtNome.getText().trim());
            projeto.setDescricao(txtDescricao.getText().trim());
            projeto.setDataInicio(dpDataInicio.getValue());
            projeto.setDataTerminoPrevista(dpDataTermino.getValue());
            projeto.setStatus(cbStatus.getValue());
            projeto.setGerente(cbGerente.getValue());
            if (projetoSelecionado == null) projeto.setDataCriacao(java.time.LocalDateTime.now());
            projetoRepository.salvar(projeto);
            carregarProjetos();
            limparFormulario();
            mostrarMensagemInfo("Projeto salvo com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao salvar projeto: " + e.getMessage());
        }
    }

    private void excluirProjeto() {
        Projeto projeto = tblProjetos.getSelectionModel().getSelectedItem();
        if (projeto == null) {
            mostrarMensagemInfo("Selecione um projeto para excluir.");
            return;
        }
        try {
            projetoRepository.excluir(projeto.getId());
            carregarProjetos();
            limparFormulario();
            mostrarMensagemInfo("Projeto excluído com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao excluir projeto: " + e.getMessage());
        }
    }

    private void limparFormulario() {
        txtNome.clear();
        txtDescricao.clear();
        dpDataInicio.setValue(null);
        dpDataTermino.setValue(null);
        cbStatus.setValue(null);
        cbGerente.setValue(null);
        projetoSelecionado = null;
    }

    private boolean validarCampos() {
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            mostrarMensagemInfo("Nome do projeto é obrigatório.");
            txtNome.requestFocus();
            return false;
        }
        if (dpDataInicio.getValue() == null) {
            mostrarMensagemInfo("Data de início é obrigatória.");
            dpDataInicio.requestFocus();
            return false;
        }
        if (cbStatus.getValue() == null) {
            mostrarMensagemInfo("Status é obrigatório.");
            cbStatus.requestFocus();
            return false;
        }
        if (cbGerente.getValue() == null) {
            mostrarMensagemInfo("Selecione o gerente responsável.");
            cbGerente.requestFocus();
            return false;
        }
        return true;
    }

    private void mostrarMensagemInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
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
