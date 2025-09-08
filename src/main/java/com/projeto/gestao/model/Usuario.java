package com.projeto.gestao.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String cargo;

    @Column(nullable = false, unique = true, length = 50)
    private String login;

    @Column(nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerfilUsuario perfil;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "ativo")
    private Boolean ativo;

    @OneToMany(mappedBy = "gerente", cascade = CascadeType.ALL)
    private Set<Projeto> projetosGerenciados = new HashSet<>();

    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL)
    private Set<Tarefa> tarefasResponsavel = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Set<UsuarioEquipe> equipesParticipante = new HashSet<>();

    public Usuario() {
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public PerfilUsuario getPerfil() { return perfil; }
    public void setPerfil(PerfilUsuario perfil) { this.perfil = perfil; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public Set<Projeto> getProjetosGerenciados() { return projetosGerenciados; }
    public void setProjetosGerenciados(Set<Projeto> projetosGerenciados) { this.projetosGerenciados = projetosGerenciados; }

    public Set<Tarefa> getTarefasResponsavel() { return tarefasResponsavel; }
    public void setTarefasResponsavel(Set<Tarefa> tarefasResponsavel) { this.tarefasResponsavel = tarefasResponsavel; }

    public Set<UsuarioEquipe> getEquipesParticipante() { return equipesParticipante; }
    public void setEquipesParticipante(Set<UsuarioEquipe> equipesParticipante) { this.equipesParticipante = equipesParticipante; }
}
