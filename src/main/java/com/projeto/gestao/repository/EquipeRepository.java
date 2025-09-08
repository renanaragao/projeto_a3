package com.projeto.gestao.repository;

import com.projeto.gestao.model.Equipe;
import com.projeto.gestao.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class EquipeRepository {

    public void salvar(Equipe equipe) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(equipe);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao salvar equipe", e);
        }
    }

    public Optional<Equipe> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Equipe equipe = session.get(Equipe.class, id);
            return Optional.ofNullable(equipe);
        }
    }

    public List<Equipe> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Equipe> query = session.createQuery("FROM Equipe WHERE ativa = true ORDER BY nome", Equipe.class);
            return query.list();
        }
    }

    public void excluir(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Equipe equipe = session.get(Equipe.class, id);
            if (equipe != null) {
                equipe.setAtiva(false);
                session.update(equipe);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao excluir equipe", e);
        }
    }
}
