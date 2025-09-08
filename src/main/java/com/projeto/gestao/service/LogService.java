package com.projeto.gestao.service;

import com.projeto.gestao.model.LogAtividade;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LogService {

    public void registrarLogin(Usuario usuario) {
        registrarAtividade(usuario, "LOGIN", "Usuario", usuario.getId(), "Usuário realizou login");
    }

    public void registrarLogout(Usuario usuario) {
        registrarAtividade(usuario, "LOGOUT", "Usuario", usuario.getId(), "Usuário realizou logout");
    }

    public void registrarCriacao(String entidade, Long entidadeId, String descricao) {
        registrarAtividade(null, "CREATE", entidade, entidadeId, descricao);
    }

    public void registrarAtualizacao(String entidade, Long entidadeId, String descricao) {
        registrarAtividade(null, "UPDATE", entidade, entidadeId, descricao);
    }

    public void registrarExclusao(String entidade, Long entidadeId, String descricao) {
        registrarAtividade(null, "DELETE", entidade, entidadeId, descricao);
    }

    public void registrarAtividade(Usuario usuario, String acao, String entidade, Long entidadeId, String descricao) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            LogAtividade log = new LogAtividade(usuario, acao, entidade, entidadeId, descricao);
            session.save(log);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Erro ao registrar log: " + e.getMessage());
        }
    }
}
