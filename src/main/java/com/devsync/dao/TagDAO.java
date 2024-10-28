package com.devsync.dao;

import com.devsync.model.Tag;
import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TagDAO {
    private static final Logger logger = Logger.getLogger(TagDAO.class.getName());

    public static List<Tag> getAllTags() {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<Tag> query = em.createQuery("SELECT t FROM Tag t ORDER BY t.name", Tag.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public static Tag findTagById(Long id) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            return em.find(Tag.class, id);
        } finally {
            em.close();
        }
    }

    public static void createTag(Tag tag) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tag);
            em.getTransaction().commit();
            logger.info("Tag created successfully with ID: " + tag.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.log(Level.SEVERE, "Error creating tag", e);
            throw e;
        } finally {
            em.close();
        }
    }
}