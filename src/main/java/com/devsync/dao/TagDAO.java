package com.devsync.dao;

import com.devsync.model.Tag;
import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TagDAO {

    public static List<Tag> getAllTags() {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<Tag> query = em.createQuery("SELECT t FROM Tag t", Tag.class);
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
        } finally {
            em.close();
        }
    }
}