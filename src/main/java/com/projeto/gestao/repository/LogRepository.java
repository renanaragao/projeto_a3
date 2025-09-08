package com.projeto.gestao.repository;

import com.projeto.gestao.model.LogAtividade;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;

public class LogRepository {

    public void salvar(LogAtividade log) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(log);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao salvar log", e);
        }
    }

    public List<LogAtividade> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<LogAtividade> query = session.createQuery("FROM LogAtividade ORDER BY dataAcao DESC", LogAtividade.class);
            return query.list();
        }
    }

    public List<LogAtividade> listarPorUsuario(Usuario usuario) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<LogAtividade> query = session.createQuery("FROM LogAtividade WHERE usuario = :usuario ORDER BY dataAcao DESC", LogAtividade.class);
            query.setParameter("usuario", usuario);
            return query.list();
        }
    }

    public List<LogAtividade> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<LogAtividade> query = session.createQuery("FROM LogAtividade WHERE dataAcao BETWEEN :inicio AND :fim ORDER BY dataAcao DESC", LogAtividade.class);
            query.setParameter("inicio", inicio);
            query.setParameter("fim", fim);
            return query.list();
        }
    }
}
