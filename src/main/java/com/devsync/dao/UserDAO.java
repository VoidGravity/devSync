package com.devsync.dao;

import com.devsync.model.User;
import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;


public class UserDAO {

    static EntityManagerFactory emf= JPAutil.EMF();
    static List<User> users = new ArrayList<>();


    public static void create(User user){

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
    }
    public static List<User> getUsers(){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        String q = "select u from User u";
        users = em.createQuery(q,User.class).getResultList();
        em.getTransaction().commit();

        em.close();
        return users;
    }
    public static User findUser(User user){
        EntityManager em= emf.createEntityManager();
        em.getTransaction().begin();
        user=em.find(user.getClass(),user.getId());
        em.getTransaction().commit();
        em.close();
        return user;
    }
    public static void deleteUser(User user){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.find(User.class,user.getId());

        em.remove(em.find(User.class,user.getId()));
        em.getTransaction().commit();
        em.close();
    }

    public static void updateUser(User user){
        EntityManager em=emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
        em.close();
    }
}
