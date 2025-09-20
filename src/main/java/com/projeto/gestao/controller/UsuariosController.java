package com.projeto.gestao.controller;

import com.projeto.gestao.model.PerfilUsuario;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.service.UsuarioService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuariosController extends BaseController {

    @FXML
    private TableView<Usuario> tblUsuarios;
    @FXML
    private TableColumn<Usuario, String> colNome;
    @FXML
    private TableColumn<Usuario, String> colLogin;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, PerfilUsuario> colPerfil;
    @FXML
    private TableColumn<Usuario, Boolean> colAtivo;

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtCargo;
    @FXML
    private TextField txtLogin;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private ComboBox<PerfilUsuario> cmbPerfil;
    @FXML
    private CheckBox chkAtivo;

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnSalvar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnCancelar;

    private final UsuarioService usuarioService = new UsuarioService();
    private final ObservableList<Usuario> usuarios = FXCollections.observableArrayList();
    private Usuario usuarioSelecionado;

    @Override
    public void configurarTela() {
        // Sem regras específicas por perfil neste momento
    }

    @FXML
    private void initialize() {
        // Colunas da tabela
        colNome.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nomeCompleto"));
        colLogin.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("login"));
        colEmail.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("email"));
        colPerfil.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("perfil"));
        colAtivo.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("ativo"));

        tblUsuarios.setItems(usuarios);
        cmbPerfil.setItems(FXCollections.observableArrayList(PerfilUsuario.values()));

        // Eventos
        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> preencherFormulario(newSel));
        btnNovo.setOnAction(e -> novoUsuario());
        btnSalvar.setOnAction(e -> salvarUsuario());
        btnExcluir.setOnAction(e -> excluirUsuario());
        btnCancelar.setOnAction(e -> limparFormulario());

        carregarUsuarios();
        novoUsuario();
    }

    private void carregarUsuarios() {
        usuarios.setAll(usuarioService.listarTodos());
    }

    private void preencherFormulario(Usuario usuario) {
        usuarioSelecionado = usuario;
        if (usuario == null) return;
        txtNome.setText(usuario.getNomeCompleto());
        txtCpf.setText(usuario.getCpf());
        txtEmail.setText(usuario.getEmail());
        txtCargo.setText(usuario.getCargo());
        txtLogin.setText(usuario.getLogin());
        txtSenha.clear(); // por segurança
        cmbPerfil.setValue(usuario.getPerfil());
        chkAtivo.setSelected(Boolean.TRUE.equals(usuario.getAtivo()));
    }

    private void novoUsuario() {
        usuarioSelecionado = null;
        txtNome.clear();
        txtCpf.clear();
        txtEmail.clear();
        txtCargo.clear();
        txtLogin.clear();
        txtSenha.clear();
        cmbPerfil.getSelectionModel().clearSelection();
        chkAtivo.setSelected(true);
        tblUsuarios.getSelectionModel().clearSelection();
    }

    private void salvarUsuario() {
        try {
            if (!validarCampos()) return;

            if (usuarioSelecionado == null) {
                Usuario novo = new Usuario();
                preencherDadosUsuario(novo, true);
                usuarioService.criarUsuario(novo);
                mostrarInfo("Usuário criado com sucesso.");
            } else {
                Usuario edit = usuarioService.buscarPorId(usuarioSelecionado.getId())
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado para edição"));
                preencherDadosUsuario(edit, false);
                usuarioService.atualizarUsuario(edit);
                mostrarInfo("Usuário atualizado com sucesso.");
            }

            carregarUsuarios();
            novoUsuario();
        } catch (Exception ex) {
            mostrarErro("Erro ao salvar usuário: " + ex.getMessage());
        }
    }

    private void excluirUsuario() {
        try {
            Usuario selecionado = tblUsuarios.getSelectionModel().getSelectedItem();
            if (selecionado == null) {
                mostrarAlerta("Selecione um usuário para excluir.");
                return;
            }
            Alert conf = new Alert(Alert.AlertType.CONFIRMATION, "Confirma a exclusão do usuário?", ButtonType.YES, ButtonType.NO);
            conf.setHeaderText(null);
            conf.showAndWait();
            if (conf.getResult() == ButtonType.YES) {
                usuarioService.excluirUsuario(selecionado.getId());
                mostrarInfo("Usuário excluído com sucesso.");
                carregarUsuarios();
                novoUsuario();
            }
        } catch (Exception ex) {
            mostrarErro("Erro ao excluir usuário: " + ex.getMessage());
        }
    }

    private boolean validarCampos() {
        if (vazio(txtNome) || vazio(txtCpf) || vazio(txtEmail) || vazio(txtCargo) || vazio(txtLogin) || cmbPerfil.getValue() == null) {
            mostrarAlerta("Preencha todos os campos obrigatórios.");
            return false;
        }
        if (usuarioSelecionado == null && vazio(txtSenha)) {
            mostrarAlerta("Senha é obrigatória para novo usuário.");
            return false;
        }
        return true;
    }

    private boolean vazio(TextField t) {
        return t.getText() == null || t.getText().trim().isEmpty();
    }

    private void preencherDadosUsuario(Usuario u, boolean novo) {
        u.setNomeCompleto(txtNome.getText().trim());
        u.setCpf(txtCpf.getText().trim());
        u.setEmail(txtEmail.getText().trim());
        u.setCargo(txtCargo.getText().trim());
        u.setLogin(txtLogin.getText().trim());
        u.setPerfil(cmbPerfil.getValue());
        u.setAtivo(chkAtivo.isSelected());

        String senhaDigitada = txtSenha.getText();
        if (novo) {
            u.setSenha(senhaDigitada);
        } else {
            if (senhaDigitada != null && !senhaDigitada.trim().isEmpty()) {
                u.setSenha(senhaDigitada);
            } else {
                u.setSenha(usuarioSelecionado.getSenha());
            }
        }
    }

    private void limparFormulario() {
        novoUsuario();
    }

    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    private void mostrarAlerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    private void mostrarErro(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
