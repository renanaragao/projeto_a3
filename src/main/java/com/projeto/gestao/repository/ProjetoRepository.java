package com.projeto.gestao.repository;

import com.projeto.gestao.model.Projeto;
import com.projeto.gestao.model.StatusProjeto;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ProjetoRepository {

    public void salvar(Projeto projeto) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(projeto);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao salvar projeto", e);
        }
    }

    public Optional<Projeto> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Projeto projeto = session.get(Projeto.class, id);
            return Optional.ofNullable(projeto);
        }
    }

    public List<Projeto> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Projeto> query = session.createQuery("FROM Projeto WHERE excluido = false ORDER BY dataCriacao DESC", Projeto.class);
            return query.list();
        }
    }

    public List<Projeto> listarPorGerente(Usuario gerente) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Projeto> query = session.createQuery("FROM Projeto WHERE gerente = :gerente ORDER BY dataCriacao DESC", Projeto.class);
            query.setParameter("gerente", gerente);
            return query.list();
        }
    }

    public List<Projeto> listarPorStatus(StatusProjeto status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Projeto> query = session.createQuery("FROM Projeto WHERE status = :status ORDER BY dataCriacao DESC", Projeto.class);
            query.setParameter("status", status);
            return query.list();
        }
    }

    public List<Projeto> listarProjetosComRiscoAtraso() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate hoje = LocalDate.now();
            Query<Projeto> query = session.createQuery(
                "FROM Projeto WHERE dataTerminoPrevista < :hoje AND status IN (:statusAtivos) ORDER BY dataTerminoPrevista",
                Projeto.class
            );
            query.setParameter("hoje", hoje);
            query.setParameter("statusAtivos", List.of(StatusProjeto.PLANEJADO, StatusProjeto.EM_ANDAMENTO));
            return query.list();
        }
    }

    public void excluir(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Projeto projeto = session.get(Projeto.class, id);
            if (projeto != null) {
                projeto.setExcluido(true);
                session.update(projeto);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao excluir projeto", e);
        }
    }
}
