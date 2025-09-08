package com.projeto.gestao.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projetos")
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_termino_prevista")
    private LocalDate dataTerminoPrevista;

    @Column(name = "data_termino_real")
    private LocalDate dataTerminoReal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProjeto status;

    @ManyToOne
    @JoinColumn(name = "gerente_id", nullable = false)
    private Usuario gerente;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private Set<Tarefa> tarefas = new HashSet<>();

    @OneToMany(mappedBy = "projeto", cascade = CascadeType.ALL)
    private Set<ProjetoEquipe> equipes = new HashSet<>();

    public Projeto() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusProjeto.PLANEJADO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataTerminoPrevista() { return dataTerminoPrevista; }
    public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) { this.dataTerminoPrevista = dataTerminoPrevista; }

    public LocalDate getDataTerminoReal() { return dataTerminoReal; }
    public void setDataTerminoReal(LocalDate dataTerminoReal) { this.dataTerminoReal = dataTerminoReal; }

    public StatusProjeto getStatus() { return status; }
    public void setStatus(StatusProjeto status) { this.status = status; }

    public Usuario getGerente() { return gerente; }
    public void setGerente(Usuario gerente) { this.gerente = gerente; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Set<Tarefa> getTarefas() { return tarefas; }
    public void setTarefas(Set<Tarefa> tarefas) { this.tarefas = tarefas; }

    public Set<ProjetoEquipe> getEquipes() { return equipes; }
    public void setEquipes(Set<ProjetoEquipe> equipes) { this.equipes = equipes; }
}
