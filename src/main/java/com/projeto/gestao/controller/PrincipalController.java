package com.projeto.gestao.controller;

import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.PerfilUsuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class PrincipalController {
    @FXML private Label lblUsuarioLogado;
    @FXML private Button btnUsuarios;
    @FXML private Button btnProjetos;
    @FXML private Button btnEquipes;
    @FXML private Button btnTarefas;
    @FXML private Button btnKanban;
    @FXML private Button btnRelatorios;
    @FXML private Button btnSair;
    @FXML private AnchorPane paneConteudo;
    @FXML private VBox menuLateral;

    private Usuario usuarioLogado;

    @FXML
    private void initialize() {
        btnUsuarios.setOnAction(e -> carregarTela("/fxml/Usuarios.fxml"));
        btnEquipes.setOnAction(e -> carregarTela("/fxml/Equipes.fxml"));
        btnProjetos.setOnAction(e -> carregarTela("/fxml/Projetos.fxml"));
        btnTarefas.setOnAction(e -> carregarTela("/fxml/Tarefas.fxml"));
        btnKanban.setOnAction(e -> carregarTela("/fxml/Kanban.fxml"));
        btnRelatorios.setOnAction(e -> carregarTela("/fxml/Relatorios.fxml"));
        btnSair.setOnAction(e -> sair());
    }

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        lblUsuarioLogado.setText(usuario.getNomeCompleto() + " (" + usuario.getPerfil() + ")");
        configurarPermissoes();
        carregarTelaDashboard();
    }

    private void configurarPermissoes() {
        if (usuarioLogado.getPerfil() == PerfilUsuario.COLABORADOR) {
            btnUsuarios.setVisible(false);
            btnEquipes.setDisable(true);
        } else if (usuarioLogado.getPerfil() == PerfilUsuario.GERENTE) {
            btnUsuarios.setDisable(true);
        }
    }

    private void carregarTelaDashboard() {
        carregarTela("/fxml/Dashboard.fxml");
    }

    private void carregarTela(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent tela = loader.load();

            Object controller = loader.getController();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setUsuarioLogado(usuarioLogado);
            }

            paneConteudo.getChildren().clear();
            paneConteudo.getChildren().add(tela);
            AnchorPane.setTopAnchor(tela, 0.0);
            AnchorPane.setBottomAnchor(tela, 0.0);
            AnchorPane.setLeftAnchor(tela, 0.0);
            AnchorPane.setRightAnchor(tela, 0.0);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar tela");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void sair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Saída");
        alert.setHeaderText("Deseja realmente sair do sistema?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
