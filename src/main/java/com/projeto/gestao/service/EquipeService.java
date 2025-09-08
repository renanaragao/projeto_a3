package com.projeto.gestao.service;

import com.projeto.gestao.model.Equipe;
import com.projeto.gestao.repository.EquipeRepository;
import java.util.List;
import java.util.Optional;

public class EquipeService {
    private final EquipeRepository equipeRepository;
    private final LogService logService;

    public EquipeService() {
        this.equipeRepository = new EquipeRepository();
        this.logService = new LogService();
    }

    public void criarEquipe(Equipe equipe) {
        validarEquipe(equipe);
        equipeRepository.salvar(equipe);
        logService.registrarCriacao("Equipe", equipe.getId(), "Equipe criada: " + equipe.getNome());
    }

    public void atualizarEquipe(Equipe equipe) {
        validarEquipe(equipe);
        equipeRepository.salvar(equipe);
        logService.registrarAtualizacao("Equipe", equipe.getId(), "Equipe atualizada: " + equipe.getNome());
    }

    public List<Equipe> listarTodas() {
        return equipeRepository.listarTodas();
    }

    public Optional<Equipe> buscarPorId(Long id) {
        return equipeRepository.buscarPorId(id);
    }

    public void excluirEquipe(Long id) {
        Equipe equipe = equipeRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));
        equipeRepository.excluir(id);
        logService.registrarExclusao("Equipe", id, "Equipe excluída: " + equipe.getNome());
    }

    private void validarEquipe(Equipe equipe) {
        if (equipe.getNome() == null || equipe.getNome().trim().isEmpty()) {
            throw new RuntimeException("Nome da equipe é obrigatório");
        }
    }
}
