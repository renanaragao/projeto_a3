package com.projeto.gestao.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Usuario responsavel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTarefa status;

    @Column(name = "data_inicio_prevista")
    private LocalDate dataInicioPrevista;

    @Column(name = "data_fim_prevista")
    private LocalDate dataFimPrevista;

    @Column(name = "data_inicio_real")
    private LocalDate dataInicioReal;

    @Column(name = "data_fim_real")
    private LocalDate dataFimReal;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "prioridade")
    @Enumerated(EnumType.STRING)
    private PrioridadeTarefa prioridade;

    @Column(name = "posicao_kanban")
    private Integer posicaoKanban;

    public Tarefa() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusTarefa.TODO;
        this.prioridade = PrioridadeTarefa.MEDIA;
        this.posicaoKanban = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }

    public Usuario getResponsavel() { return responsavel; }
    public void setResponsavel(Usuario responsavel) { this.responsavel = responsavel; }

    public StatusTarefa getStatus() { return status; }
    public void setStatus(StatusTarefa status) { this.status = status; }

    public LocalDate getDataInicioPrevista() { return dataInicioPrevista; }
    public void setDataInicioPrevista(LocalDate dataInicioPrevista) { this.dataInicioPrevista = dataInicioPrevista; }

    public LocalDate getDataFimPrevista() { return dataFimPrevista; }
    public void setDataFimPrevista(LocalDate dataFimPrevista) { this.dataFimPrevista = dataFimPrevista; }

    public LocalDate getDataInicioReal() { return dataInicioReal; }
    public void setDataInicioReal(LocalDate dataInicioReal) { this.dataInicioReal = dataInicioReal; }

    public LocalDate getDataFimReal() { return dataFimReal; }
    public void setDataFimReal(LocalDate dataFimReal) { this.dataFimReal = dataFimReal; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public PrioridadeTarefa getPrioridade() { return prioridade; }
    public void setPrioridade(PrioridadeTarefa prioridade) { this.prioridade = prioridade; }

    public Integer getPosicaoKanban() { return posicaoKanban; }
    public void setPosicaoKanban(Integer posicaoKanban) { this.posicaoKanban = posicaoKanban; }
}
