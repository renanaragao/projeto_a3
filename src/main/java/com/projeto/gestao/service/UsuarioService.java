package com.projeto.gestao.service;

import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.PerfilUsuario;
import com.projeto.gestao.repository.UsuarioRepository;
import com.projeto.gestao.util.CriptografiaUtil;
import java.util.List;
import java.util.Optional;

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final LogService logService;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
        this.logService = new LogService();
    }

    public Optional<Usuario> autenticar(String login, String senha) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorLogin(login);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (CriptografiaUtil.verificarSenha(senha, usuario.getSenha())) {
                logService.registrarLogin(usuario);
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    public void criarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        usuario.setSenha(CriptografiaUtil.criptografarSenha(usuario.getSenha()));
        usuarioRepository.salvar(usuario);
        logService.registrarCriacao("Usuario", usuario.getId(), "Usuário criado: " + usuario.getNomeCompleto());
    }

    public void atualizarUsuario(Usuario usuario) {
        validarUsuario(usuario);
        Usuario usuarioExistente = usuarioRepository.buscarPorId(usuario.getId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!usuario.getSenha().equals(usuarioExistente.getSenha())) {
            usuario.setSenha(CriptografiaUtil.criptografarSenha(usuario.getSenha()));
        }

        usuarioRepository.salvar(usuario);
        logService.registrarAtualizacao("Usuario", usuario.getId(), "Usuário atualizado: " + usuario.getNomeCompleto());
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }

    public List<Usuario> listarGerentes() {
        return usuarioRepository.listarPorPerfil(PerfilUsuario.GERENTE);
    }

    public List<Usuario> listarColaboradores() {
        return usuarioRepository.listarPorPerfil(PerfilUsuario.COLABORADOR);
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.buscarPorId(id);
    }

    public void excluirUsuario(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.excluir(id);
        logService.registrarExclusao("Usuario", id, "Usuário excluído: " + usuario.getNomeCompleto());
    }

    public int contarUsuarios() {
        return listarTodos().size();
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario.getNomeCompleto() == null || usuario.getNomeCompleto().trim().isEmpty()) {
            throw new RuntimeException("Nome completo é obrigatório");
        }
        if (usuario.getLogin() == null || usuario.getLogin().trim().isEmpty()) {
            throw new RuntimeException("Login é obrigatório");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email é obrigatório");
        }
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new RuntimeException("Senha é obrigatória");
        }
        if (usuario.getPerfil() == null) {
            throw new RuntimeException("Perfil é obrigatório");
        }

        if (usuario.getId() == null) {
            if (usuarioRepository.existeLogin(usuario.getLogin())) {
                throw new RuntimeException("Login já existe");
            }
            if (usuarioRepository.existeEmail(usuario.getEmail())) {
                throw new RuntimeException("Email já existe");
            }
        }
    }
}
