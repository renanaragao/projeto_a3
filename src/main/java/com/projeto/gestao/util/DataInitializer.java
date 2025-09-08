package com.projeto.gestao.util;

import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.PerfilUsuario;
import com.projeto.gestao.service.UsuarioService;

public class DataInitializer {
    private final UsuarioService usuarioService = new UsuarioService();

    public void inicializarDados() {
        try {
            criarUsuarioAdministrador();
            criarUsuarioGerente();
            criarUsuarioColaborador();
        } catch (Exception e) {
            System.out.println("Dados já inicializados ou erro: " + e.getMessage());
        }
    }

    private void criarUsuarioAdministrador() {
        if (!usuarioService.listarTodos().stream()
                .anyMatch(u -> u.getLogin().equals("admin"))) {

            Usuario admin = new Usuario();
            admin.setNomeCompleto("Administrador do Sistema");
            admin.setCpf("00000000000");
            admin.setEmail("admin@sistema.com");
            admin.setCargo("Administrador");
            admin.setLogin("admin");
            admin.setSenha("admin123");
            admin.setPerfil(PerfilUsuario.ADMINISTRADOR);

            usuarioService.criarUsuario(admin);
            System.out.println("Usuário administrador criado - Login: admin / Senha: admin123");
        }
    }

    private void criarUsuarioGerente() {
        if (!usuarioService.listarTodos().stream()
                .anyMatch(u -> u.getLogin().equals("gerente"))) {

            Usuario gerente = new Usuario();
            gerente.setNomeCompleto("João Silva");
            gerente.setCpf("12345678901");
            gerente.setEmail("joao@empresa.com");
            gerente.setCargo("Gerente de Projetos");
            gerente.setLogin("gerente");
            gerente.setSenha("gerente123");
            gerente.setPerfil(PerfilUsuario.GERENTE);

            usuarioService.criarUsuario(gerente);
            System.out.println("Usuário gerente criado - Login: gerente / Senha: gerente123");
        }
    }

    private void criarUsuarioColaborador() {
        if (!usuarioService.listarTodos().stream()
                .anyMatch(u -> u.getLogin().equals("colaborador"))) {

            Usuario colaborador = new Usuario();
            colaborador.setNomeCompleto("Maria Santos");
            colaborador.setCpf("98765432100");
            colaborador.setEmail("maria@empresa.com");
            colaborador.setCargo("Desenvolvedora");
            colaborador.setLogin("colaborador");
            colaborador.setSenha("colaborador123");
            colaborador.setPerfil(PerfilUsuario.COLABORADOR);

            usuarioService.criarUsuario(colaborador);
            System.out.println("Usuário colaborador criado - Login: colaborador / Senha: colaborador123");
        }
    }
}
