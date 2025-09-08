package com.projeto.gestao.controller;

import com.projeto.gestao.service.ProjetoService;
import com.projeto.gestao.service.EquipeService;
import com.projeto.gestao.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController extends BaseController {
    @FXML private Label lblTotalProjetos;
    @FXML private Label lblTotalTimes;
    @FXML private Label lblTotalUsuarios;

    private final ProjetoService projetoService = new ProjetoService();
    private final EquipeService equipeService = new EquipeService();
    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void initialize() {
        try {
            int totalProjetos = projetoService.contarProjetos();
            int totalTimes = equipeService.contarEquipes();
            int totalUsuarios = usuarioService.contarUsuarios();

            lblTotalProjetos.setText(String.valueOf(totalProjetos));
            lblTotalTimes.setText(String.valueOf(totalTimes));
            lblTotalUsuarios.setText(String.valueOf(totalUsuarios));
        } catch (Exception e) {
            lblTotalProjetos.setText("Erro: " + e.getMessage());
            lblTotalTimes.setText("-");
            lblTotalUsuarios.setText("-");
            e.printStackTrace();
        }
    }

    @Override
    public void configurarTela() {
        // Não é necessário configuração extra para o dashboard
    }
}
