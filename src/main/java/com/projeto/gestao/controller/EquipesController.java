package com.projeto.gestao.controller;

import com.projeto.gestao.model.Equipe;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.repository.EquipeRepository;
import com.projeto.gestao.repository.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EquipesController extends BaseController {
    @FXML private TableView<Equipe> tblEquipes;
    @FXML private TableColumn<Equipe, String> colNome;
    @FXML private TableColumn<Equipe, String> colDescricao;
    @FXML private TableColumn<Equipe, Boolean> colAtiva;
    @FXML private TableColumn<Equipe, String> colDataCriacao;
    @FXML private TableColumn<Equipe, Integer> colQtdMembros;

    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;
    @FXML private CheckBox chkAtiva;
    @FXML private ListView<Usuario> lstMembros;
    @FXML private Button btnNovo;
    @FXML private Button btnSalvar;
    @FXML private Button btnExcluir;
    @FXML private Button btnCancelar;

    @FXML private TextField filtroNome;
    @FXML private ComboBox<String> filtroStatus;
    @FXML private ComboBox<Usuario> filtroMembro;
    @FXML private Button btnFiltrar;
    @FXML private Button btnLimparFiltro;

    private final EquipeRepository equipeRepository = new EquipeRepository();
    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final ObservableList<Equipe> equipes = FXCollections.observableArrayList();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private Equipe equipeSelecionada;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarEquipes();
        carregarUsuarios();
        configurarFormulario();
        configurarFiltros();
        configurarAcoes();
    }

    @Override
    protected void configurarTela() {}

    private void configurarTabela() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colAtiva.setCellValueFactory(new PropertyValueFactory<>("ativa"));
        colDataCriacao.setCellValueFactory(cellData -> {
            var dt = cellData.getValue().getDataCriacao();
            String formatted = dt != null ? dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "";
            return new javafx.beans.property.SimpleStringProperty(formatted);
        });
        colQtdMembros.setCellValueFactory(cellData -> {
            Long equipeId = cellData.getValue().getId();
            int count = equipeId != null ? equipeRepository.contarMembrosAtivos(equipeId) : 0;
            return new javafx.beans.property.SimpleIntegerProperty(count).asObject();
        });

        tblEquipes.setItems(equipes);
        tblEquipes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> selecionarEquipe(newSel));

        // Garantir que a tabela seja visível
        tblEquipes.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblEquipes.setPrefHeight(300);
        tblEquipes.setMinHeight(200);
    }

    private void carregarEquipes() {
        try {
            List<Equipe> listaEquipes = equipeRepository.listarTodas();
            System.out.println("Carregando " + listaEquipes.size() + " equipes");
            equipes.setAll(listaEquipes);

            // Se não há equipes, adicionar uma mensagem no console
            if (listaEquipes.isEmpty()) {
                System.out.println("Nenhuma equipe encontrada no banco de dados");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar equipes: " + e.getMessage());
            e.printStackTrace();
            mostrarMensagemErro("Erro ao carregar equipes: " + e.getMessage());
        }
    }

    private void carregarUsuarios() {
        usuarios.setAll(usuarioRepository.listarTodos());
        lstMembros.setItems(usuarios);
        lstMembros.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstMembros.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Usuario> call(ListView<Usuario> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Usuario item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getNomeCompleto());
                    }
                };
            }
        });
        filtroMembro.setItems(usuarios);
        filtroMembro.setCellFactory(lstMembros.getCellFactory());
        filtroMembro.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Usuario item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNomeCompleto());
            }
        });
    }

    private void configurarFormulario() {
        limparFormulario();
    }

    private void configurarFiltros() {
        filtroStatus.setItems(FXCollections.observableArrayList("Ativa", "Inativa"));
        btnFiltrar.setOnAction(e -> aplicarFiltro());
        btnLimparFiltro.setOnAction(e -> limparFiltro());
    }

    private void configurarAcoes() {
        btnNovo.setOnAction(e -> novo());
        btnSalvar.setOnAction(e -> salvar());
        btnExcluir.setOnAction(e -> excluir());
        btnCancelar.setOnAction(e -> limparFormulario());
    }

    private void selecionarEquipe(Equipe equipe) {
        equipeSelecionada = equipe;
        if (equipe == null) {
            limparFormulario();
            return;
        }
        txtNome.setText(equipe.getNome());
        txtDescricao.setText(equipe.getDescricao());
        chkAtiva.setSelected(Boolean.TRUE.equals(equipe.getAtiva()));
        lstMembros.getSelectionModel().clearSelection();
        if (equipe.getMembros() != null) {
            List<Usuario> membros = equipe.getMembros().stream()
                .map(ue -> ue.getUsuario())
                .collect(Collectors.toList());
            for (Usuario u : usuarios) {
                if (membros.stream().anyMatch(m -> m.getId().equals(u.getId()))) {
                    lstMembros.getSelectionModel().select(u);
                }
            }
        }
    }

    private void novo() {
        tblEquipes.getSelectionModel().clearSelection();
        limparFormulario();
    }

    private void salvar() {
        if (!validarCampos()) return;
        try {
            Equipe equipe = equipeSelecionada != null ? equipeSelecionada : new Equipe();
            equipe.setNome(txtNome.getText().trim());
            equipe.setDescricao(txtDescricao.getText().trim());
            equipe.setAtiva(chkAtiva.isSelected());
            // Atualizar membros
            atualizarMembrosEquipe(equipe, lstMembros.getSelectionModel().getSelectedItems());
            equipeRepository.salvar(equipe);
            carregarEquipes();
            limparFormulario();
            mostrarMensagemInfo("Equipe salva com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao salvar equipe: " + e.getMessage());
        }
    }

    private void atualizarMembrosEquipe(Equipe equipe, List<Usuario> usuariosSelecionados) {
        // Remove todos os membros antigos
        if (equipe.getMembros() != null) {
            equipe.getMembros().clear();
        }
        // Adiciona os novos membros
        for (Usuario usuario : usuariosSelecionados) {
            com.projeto.gestao.model.UsuarioEquipe ue = new com.projeto.gestao.model.UsuarioEquipe();
            ue.setUsuario(usuario);
            ue.setEquipe(equipe);
            equipe.getMembros().add(ue);
        }
    }

    private void excluir() {
        Equipe equipe = tblEquipes.getSelectionModel().getSelectedItem();
        if (equipe == null) {
            mostrarMensagemInfo("Selecione uma equipe para excluir.");
            return;
        }
        try {
            equipeRepository.excluir(equipe.getId());
            carregarEquipes();
            limparFormulario();
            mostrarMensagemInfo("Equipe excluída com sucesso!");
        } catch (Exception e) {
            mostrarMensagemErro("Erro ao excluir equipe: " + e.getMessage());
        }
    }

    private void aplicarFiltro() {
        String nome = filtroNome.getText() != null ? filtroNome.getText().trim().toLowerCase() : "";
        String status = filtroStatus.getValue();
        Usuario membro = filtroMembro.getValue();
        equipes.setAll(equipeRepository.listarTodas().stream().filter(eq -> {
            boolean match = true;
            if (!nome.isEmpty()) match &= eq.getNome() != null && eq.getNome().toLowerCase().contains(nome);
            if (status != null) match &= ("Ativa".equals(status) ? Boolean.TRUE.equals(eq.getAtiva()) : Boolean.FALSE.equals(eq.getAtiva()));
            if (membro != null && eq.getMembros() != null) {
                match &= eq.getMembros().stream().anyMatch(ue -> ue.getUsuario() != null && ue.getUsuario().getId().equals(membro.getId()));
            }
            return match;
        }).toList());
    }

    private void limparFiltro() {
        filtroNome.clear();
        filtroStatus.setValue(null);
        filtroMembro.setValue(null);
        equipes.setAll(equipeRepository.listarTodas());
    }

    private void limparFormulario() {
        txtNome.clear();
        txtDescricao.clear();
        chkAtiva.setSelected(true);
        lstMembros.getSelectionModel().clearSelection();
        equipeSelecionada = null;
    }

    private boolean validarCampos() {
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            mostrarMensagemInfo("Nome da equipe é obrigatório.");
            txtNome.requestFocus();
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
