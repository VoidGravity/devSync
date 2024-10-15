package com.devsync.config;

import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TestWebListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Init");
        EntityManagerFactory emf = JPAutil.EMF();
        EntityManager em = emf.createEntityManager();
        em.createQuery("select u from User u");


    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAutil.EMFclose();
        System.out.println("CLOSE");
    }
}
