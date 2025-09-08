package com.projeto.gestao.controller;

import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

public class LoginController {
    @FXML private TextField txtLogin;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnLogin;
    @FXML private Label lblMensagem;

    private UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void initialize() {
        btnLogin.setOnAction(e -> realizarLogin());
        txtSenha.setOnAction(e -> realizarLogin());
    }

    private void realizarLogin() {
        String login = txtLogin.getText().trim();
        String senha = txtSenha.getText();

        if (login.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Login e senha são obrigatórios");
            return;
        }

        try {
            Optional<Usuario> usuarioOpt = usuarioService.autenticar(login, senha);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                abrirTelaPrincipal(usuario);
            } else {
                lblMensagem.setText("Login ou senha inválidos");
            }
        } catch (Exception e) {
            lblMensagem.setText("Erro ao conectar: " + e.getMessage());
        }
    }

    private void abrirTelaPrincipal(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Principal.fxml"));
            Parent root = loader.load();

            PrincipalController controller = loader.getController();
            controller.setUsuarioLogado(usuario);

            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Sistema de Gestão de Projetos - " + usuario.getNomeCompleto());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

            ((Stage) btnLogin.getScene().getWindow()).close();

        } catch (Exception e) {
            lblMensagem.setText("Erro ao abrir tela principal: " + e.getMessage());
        }
    }
}
