package com.projeto.gestao.repository;

import com.projeto.gestao.model.Usuario;
import com.projeto.gestao.model.PerfilUsuario;
import com.projeto.gestao.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    public void salvar(Usuario usuario) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(usuario);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    public Optional<Usuario> buscarPorId(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Usuario usuario = session.get(Usuario.class, id);
            return Optional.ofNullable(usuario);
        }
    }

    public Optional<Usuario> buscarPorLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery("FROM Usuario WHERE login = :login", Usuario.class);
            query.setParameter("login", login);
            return query.uniqueResultOptional();
        }
    }

    public List<Usuario> listarTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery("FROM Usuario ORDER BY nomeCompleto", Usuario.class);
            return query.list();
        }
    }

    public List<Usuario> listarPorPerfil(PerfilUsuario perfil) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery("FROM Usuario WHERE perfil = :perfil AND ativo = true ORDER BY nomeCompleto", Usuario.class);
            query.setParameter("perfil", perfil);
            return query.list();
        }
    }

    public void excluir(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                usuario.setAtivo(false);
                session.update(usuario);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Erro ao excluir usuário", e);
        }
    }

    public boolean existeLogin(String login) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Usuario WHERE login = :login", Long.class);
            query.setParameter("login", login);
            return query.uniqueResult() > 0;
        }
    }

    public boolean existeEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Usuario WHERE email = :email", Long.class);
            query.setParameter("email", email);
            return query.uniqueResult() > 0;
        }
    }
}
