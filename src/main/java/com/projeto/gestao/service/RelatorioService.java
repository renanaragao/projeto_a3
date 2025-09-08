package com.projeto.gestao.service;

import com.projeto.gestao.model.*;
import com.projeto.gestao.repository.ProjetoRepository;
import com.projeto.gestao.repository.TarefaRepository;
import com.projeto.gestao.repository.UsuarioRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioService {
    private final ProjetoRepository projetoRepository;
    private final TarefaRepository tarefaRepository;
    private final UsuarioRepository usuarioRepository;

    public RelatorioService() {
        this.projetoRepository = new ProjetoRepository();
        this.tarefaRepository = new TarefaRepository();
        this.usuarioRepository = new UsuarioRepository();
    }

    public Map<String, Object> obterDashboardGeral() {
        Map<String, Object> dashboard = new HashMap<>();

        List<Projeto> todosProjetos = projetoRepository.listarTodos();
        List<Tarefa> todasTarefas = tarefaRepository.listarTodas();
        List<Usuario> todosUsuarios = usuarioRepository.listarTodos();

        dashboard.put("totalProjetos", todosProjetos.size());
        dashboard.put("totalTarefas", todasTarefas.size());
        dashboard.put("totalUsuarios", todosUsuarios.size());

        long projetosAtivos = todosProjetos.stream()
                .filter(p -> p.getStatus() == StatusProjeto.EM_ANDAMENTO)
                .count();
        dashboard.put("projetosAtivos", projetosAtivos);

        long tarefasConcluidas = todasTarefas.stream()
                .filter(t -> t.getStatus() == StatusTarefa.DONE)
                .count();
        dashboard.put("tarefasConcluidas", tarefasConcluidas);

        List<Projeto> projetosComRisco = projetoRepository.listarProjetosComRiscoAtraso();
        dashboard.put("projetosComRisco", projetosComRisco.size());

        return dashboard;
    }

    public Map<String, Long> obterEstatisticasTarefasPorStatus() {
        List<Tarefa> todasTarefas = tarefaRepository.listarTodas();

        return todasTarefas.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getStatus().toString(),
                        Collectors.counting()
                ));
    }

    public Map<String, Long> obterEstatisticasProjetosPorStatus() {
        List<Projeto> todosProjetos = projetoRepository.listarTodos();

        return todosProjetos.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getStatus().toString(),
                        Collectors.counting()
                ));
    }

    public Map<String, Object> obterDesempenhoColaborador(Usuario colaborador) {
        Map<String, Object> desempenho = new HashMap<>();

        List<Tarefa> tarefasDoColaborador = tarefaRepository.listarPorResponsavel(colaborador);

        desempenho.put("totalTarefas", tarefasDoColaborador.size());

        long tarefasConcluidas = tarefasDoColaborador.stream()
                .filter(t -> t.getStatus() == StatusTarefa.DONE)
                .count();
        desempenho.put("tarefasConcluidas", tarefasConcluidas);

        long tarefasEmAndamento = tarefasDoColaborador.stream()
                .filter(t -> t.getStatus() == StatusTarefa.IN_PROGRESS)
                .count();
        desempenho.put("tarefasEmAndamento", tarefasEmAndamento);

        double percentualConclusao = tarefasDoColaborador.isEmpty() ? 0 :
                (double) tarefasConcluidas / tarefasDoColaborador.size() * 100;
        desempenho.put("percentualConclusao", percentualConclusao);

        return desempenho;
    }

    public List<Projeto> obterProjetosComRiscoAtraso() {
        return projetoRepository.listarProjetosComRiscoAtraso();
    }

    public Map<String, Object> obterResumoAndamentoProjetos() {
        Map<String, Object> resumo = new HashMap<>();
        List<Projeto> todosProjetos = projetoRepository.listarTodos();

        for (Projeto projeto : todosProjetos) {
            Map<String, Object> infoProjeto = new HashMap<>();
            List<Tarefa> tarefasProjeto = tarefaRepository.listarPorProjeto(projeto);

            long tarefasConcluidas = tarefasProjeto.stream()
                    .filter(t -> t.getStatus() == StatusTarefa.DONE)
                    .count();

            double progresso = tarefasProjeto.isEmpty() ? 0 :
                    (double) tarefasConcluidas / tarefasProjeto.size() * 100;

            infoProjeto.put("totalTarefas", tarefasProjeto.size());
            infoProjeto.put("tarefasConcluidas", tarefasConcluidas);
            infoProjeto.put("progresso", progresso);
            infoProjeto.put("status", projeto.getStatus());

            resumo.put(projeto.getNome(), infoProjeto);
        }

        return resumo;
    }
}
