package com.projeto.gestao.service;

import com.projeto.gestao.model.Projeto;
import com.projeto.gestao.model.StatusProjeto;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.repository.ProjetoRepository;
import java.util.List;
import java.util.Optional;

public class ProjetoService {
    private final ProjetoRepository projetoRepository;
    private final LogService logService;

    public ProjetoService() {
        this.projetoRepository = new ProjetoRepository();
        this.logService = new LogService();
    }

    public void criarProjeto(Projeto projeto) {
        validarProjeto(projeto);
        projetoRepository.salvar(projeto);
        logService.registrarCriacao("Projeto", projeto.getId(), "Projeto criado: " + projeto.getNome());
    }

    public void atualizarProjeto(Projeto projeto) {
        validarProjeto(projeto);
        projetoRepository.salvar(projeto);
        logService.registrarAtualizacao("Projeto", projeto.getId(), "Projeto atualizado: " + projeto.getNome());
    }

    public List<Projeto> listarTodos() {
        return projetoRepository.listarTodos();
    }

    public List<Projeto> listarPorGerente(Usuario gerente) {
        return projetoRepository.listarPorGerente(gerente);
    }

    public List<Projeto> listarPorStatus(StatusProjeto status) {
        return projetoRepository.listarPorStatus(status);
    }

    public List<Projeto> listarProjetosComRiscoAtraso() {
        return projetoRepository.listarProjetosComRiscoAtraso();
    }

    public Optional<Projeto> buscarPorId(Long id) {
        return projetoRepository.buscarPorId(id);
    }

    public void cancelarProjeto(Long id) {
        Projeto projeto = projetoRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        projetoRepository.excluir(id);
        logService.registrarExclusao("Projeto", id, "Projeto cancelado: " + projeto.getNome());
    }

    public int contarProjetos() {
        return listarTodos().size();
    }

    private void validarProjeto(Projeto projeto) {
        if (projeto.getNome() == null || projeto.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome do projeto é obrigatório");
        }
        if (projeto.getGerente() == null) {
            throw new RuntimeException("Gerente responsável é obrigatório");
        }
        if (projeto.getDataInicio() == null) {
            throw new RuntimeException("Data de início é obrigatória");
        }
        if (projeto.getDataTerminoPrevista() == null) {
            throw new RuntimeException("Data de término prevista é obrigatória");
        }
        if (projeto.getDataInicio().isAfter(projeto.getDataTerminoPrevista())) {
            throw new RuntimeException("Data de início não pode ser posterior à data de término");
        }
    }
}
