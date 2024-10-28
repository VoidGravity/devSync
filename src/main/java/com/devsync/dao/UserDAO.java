package com.devsync.dao;

import com.devsync.model.User;
import com.devsync.util.JPAutil;
import jakarta.persistence.*;
import java.util.List;

public class UserDAO {
    private EntityManager em;

    public UserDAO() {
        this.em = JPAutil.EMF().createEntityManager();
    }

    public User findUser(User user) {
        if (user == null || user.getId() == null) {
            return null;
        }

        try {
            return em.find(User.class, user.getId());
        } catch (Exception e) {
            return null;
        }
    }
    public List<User> getUsersWithPendingChangeRequests() {
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.hasPendingChangeRequest = true",
                    User.class
            );
            return query.getResultList();
        } catch (Exception e) {
            return List.of(); // Return empty list instead of null
        }
    }
    public String getUserRole(User user) {
        if (user == null) return null;

        try {
            String sql = "SELECT u.role FROM User u WHERE u.username = :username AND u.password = :password";
            TypedQuery<String> query = em.createQuery(sql, String.class);
            query.setParameter("username", user.getUsername());
            query.setParameter("password", user.getPassword());
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // For testing purposes
    public UserDAO(EntityManager em) {
        this.em = em;
    }

    public boolean create(User user) {
        if (user == null) return false;

        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
    }

    public User findUserByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username",
                    User.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUserById(int id) {
        if (id <= 0) return null;

        try {
            return em.find(User.class, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> getUsers() {
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
            return query.getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public void updateUser(User user) {
//        if (user == null || user.getId() == null) return;

//        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//        }
    }

    public void deleteUser(User user) {
        if (user == null || user.getId() == null) return;

        try {
            em.getTransaction().begin();
            User managedUser = em.find(User.class, user.getId());
            if (managedUser != null) {
                em.remove(managedUser);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}