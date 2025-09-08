package com.projeto.gestao.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipes")
public class Equipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "ativa")
    private Boolean ativa;

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
    private Set<UsuarioEquipe> membros = new HashSet<>();

    @OneToMany(mappedBy = "equipe", cascade = CascadeType.ALL)
    private Set<ProjetoEquipe> projetos = new HashSet<>();

    public Equipe() {
        this.dataCriacao = LocalDateTime.now();
        this.ativa = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Boolean getAtiva() { return ativa; }
    public void setAtiva(Boolean ativa) { this.ativa = ativa; }

    public Set<UsuarioEquipe> getMembros() { return membros; }
    public void setMembros(Set<UsuarioEquipe> membros) { this.membros = membros; }

    public Set<ProjetoEquipe> getProjetos() { return projetos; }
    public void setProjetos(Set<ProjetoEquipe> projetos) { this.projetos = projetos; }
}
