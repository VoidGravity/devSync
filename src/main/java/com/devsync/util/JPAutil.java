package com.devsync.util;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.EntityManager;

@Singleton
public class JPAutil {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory EMF() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("default");
        }
        return emf;
    }

    public static EntityManager createEntityManager() {
        return EMF().createEntityManager();
    }

    public static void EMFclose() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public static void clearCache() {
        if (emf != null) {
            emf.getCache().evictAll();
        }
    }
}