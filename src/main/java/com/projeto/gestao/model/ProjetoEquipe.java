package com.projeto.gestao.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "projeto_equipe")
public class ProjetoEquipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projeto_id", nullable = false)
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;

    @Column(name = "data_alocacao")
    private LocalDateTime dataAlocacao;

    @Column(name = "data_remocao")
    private LocalDateTime dataRemocao;

    @Column(name = "ativa")
    private Boolean ativa;

    public ProjetoEquipe() {
        this.dataAlocacao = LocalDateTime.now();
        this.ativa = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Projeto getProjeto() { return projeto; }
    public void setProjeto(Projeto projeto) { this.projeto = projeto; }

    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }

    public LocalDateTime getDataAlocacao() { return dataAlocacao; }
    public void setDataAlocacao(LocalDateTime dataAlocacao) { this.dataAlocacao = dataAlocacao; }

    public LocalDateTime getDataRemocao() { return dataRemocao; }
    public void setDataRemocao(LocalDateTime dataRemocao) { this.dataRemocao = dataRemocao; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }
}
