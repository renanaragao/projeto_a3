package com.projeto.gestao.service;

import com.projeto.gestao.model.Tarefa;
import com.projeto.gestao.model.StatusTarefa;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.Projeto;
import com.projeto.gestao.repository.TarefaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TarefaService {
    private final TarefaRepository tarefaRepository;
    private final LogService logService;

    public TarefaService() {
        this.tarefaRepository = new TarefaRepository();
        this.logService = new LogService();
    }

    public void criarTarefa(Tarefa tarefa) {
        validarTarefa(tarefa);
        tarefaRepository.salvar(tarefa);
        logService.registrarCriacao("Tarefa", tarefa.getId(), "Tarefa criada: " + tarefa.getTitulo());
    }

    public void atualizarTarefa(Tarefa tarefa) {
        validarTarefa(tarefa);
        tarefaRepository.salvar(tarefa);
        logService.registrarAtualizacao("Tarefa", tarefa.getId(), "Tarefa atualizada: " + tarefa.getTitulo());
    }

    public void moverTarefaKanban(Long tarefaId, StatusTarefa novoStatus, Integer novaPosicao) {
        Tarefa tarefa = tarefaRepository.buscarPorId(tarefaId)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        StatusTarefa statusAnterior = tarefa.getStatus();
        tarefa.setStatus(novoStatus);
        tarefa.setPosicaoKanban(novaPosicao);

        if (novoStatus == StatusTarefa.IN_PROGRESS && tarefa.getDataInicioReal() == null) {
            tarefa.setDataInicioReal(LocalDate.now());
        }

        if (novoStatus == StatusTarefa.DONE && tarefa.getDataFimReal() == null) {
            tarefa.setDataFimReal(LocalDate.now());
        }

        tarefaRepository.salvar(tarefa);
        logService.registrarAtualizacao("Tarefa", tarefaId,
            "Tarefa movida no Kanban de " + statusAnterior + " para " + novoStatus);
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.listarTodas();
    }

    public List<Tarefa> listarPorStatus(StatusTarefa status) {
        return tarefaRepository.listarPorStatus(status);
    }

    public List<Tarefa> listarPorProjeto(Projeto projeto) {
        return tarefaRepository.listarPorProjeto(projeto);
    }

    public List<Tarefa> listarPorResponsavel(Usuario responsavel) {
        return tarefaRepository.listarPorResponsavel(responsavel);
    }

    public List<Tarefa> listarPorProjetoEStatus(Projeto projeto, StatusTarefa status) {
        return tarefaRepository.listarPorProjetoEStatus(projeto, status);
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.buscarPorId(id);
    }

    public void cancelarTarefa(Long id) {
        Tarefa tarefa = tarefaRepository.buscarPorId(id)
            .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        tarefaRepository.excluir(id);
        logService.registrarExclusao("Tarefa", id, "Tarefa cancelada: " + tarefa.getTitulo());
    }

    private void validarTarefa(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("Título da tarefa é obrigatório");
        }
        if (tarefa.getProjeto() == null) {
            throw new RuntimeException("Projeto é obrigatório");
        }
        if (tarefa.getDataInicioPrevista() != null && tarefa.getDataFimPrevista() != null) {
            if (tarefa.getDataInicioPrevista().isAfter(tarefa.getDataFimPrevista())) {
                throw new RuntimeException("Data de início prevista não pode ser posterior à data de fim prevista");
            }
        }
    }
}
