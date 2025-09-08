package com.projeto.gestao.repository;

import com.projeto.gestao.model.Tarefa;
import com.projeto.gestao.model.StatusTarefa;
import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.Projeto;
import com.projeto.gestao.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class TarefaRepository {

    public void salvar(Tarefa tarefa) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(tarefa);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao salvar tarefa", e);
        }
    }

    public Optional<Tarefa> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Tarefa tarefa = session.get(Tarefa.class, id);
            return Optional.ofNullable(tarefa);
        }
    }

    public List<Tarefa> listarTodas() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tarefa> query = session.createQuery("FROM Tarefa ORDER BY dataCriacao DESC", Tarefa.class);
            return query.list();
        }
    }

    public List<Tarefa> listarPorStatus(StatusTarefa status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tarefa> query = session.createQuery("FROM Tarefa WHERE status = :status ORDER BY posicaoKanban, dataCriacao", Tarefa.class);
            query.setParameter("status", status);
            return query.list();
        }
    }

    public List<Tarefa> listarPorProjeto(Projeto projeto) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tarefa> query = session.createQuery("FROM Tarefa WHERE projeto = :projeto ORDER BY status, posicaoKanban", Tarefa.class);
            query.setParameter("projeto", projeto);
            return query.list();
        }
    }

    public List<Tarefa> listarPorResponsavel(Usuario responsavel) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tarefa> query = session.createQuery("FROM Tarefa WHERE responsavel = :responsavel ORDER BY status, dataCriacao", Tarefa.class);
            query.setParameter("responsavel", responsavel);
            return query.list();
        }
    }

    public List<Tarefa> listarPorProjetoEStatus(Projeto projeto, StatusTarefa status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Tarefa> query = session.createQuery("FROM Tarefa WHERE projeto = :projeto AND status = :status ORDER BY posicaoKanban", Tarefa.class);
            query.setParameter("projeto", projeto);
            query.setParameter("status", status);
            return query.list();
        }
    }

    public void atualizarPosicaoKanban(Long tarefaId, Integer novaPosicao) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Tarefa tarefa = session.get(Tarefa.class, tarefaId);
            if (tarefa != null) {
                tarefa.setPosicaoKanban(novaPosicao);
                session.update(tarefa);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao atualizar posição no Kanban", e);
        }
    }

    public void excluir(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Tarefa tarefa = session.get(Tarefa.class, id);
            if (tarefa != null) {
                tarefa.setStatus(StatusTarefa.CANCELADA);
                session.update(tarefa);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao cancelar tarefa", e);
        }
    }
}
