package com.projeto.gestao.controller;

import com.projeto.gestao.model.Usuario;

public abstract class BaseController {
    protected Usuario usuarioLogado;

    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
        configurarTela();
    }

    protected abstract void configurarTela();
}
