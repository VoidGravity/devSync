package com.devsync.dao;

import com.devsync.model.User;
import com.devsync.util.JPAutil;
import jakarta.persistence.*;

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
    public static boolean login(User user){
        EntityManager em = emf.createEntityManager();
        String sql = "Select u from User u where u.password=:password and u.username=:username";

        try{
            em.createQuery(sql,User.class).setParameter("username",user.getUsername()).setParameter("password",user.getPassword()).getSingleResult();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            em.close();
        }
    }
    public static String getUserRole(User user){
        EntityManager em = emf.createEntityManager();

        String sql = "Select u.role from User u where u.password=:password and u.username=:username";
        System.out.println("here is the result : ");
        System.out.println(String.valueOf(em.createQuery(sql, User.class).setParameter("username",user.getUsername()).setParameter("password",user.getPassword()).getSingleResult()));
        System.out.println("end");
        return String.valueOf(em.createQuery(sql, User.class).setParameter("username",user.getUsername()).setParameter("password",user.getPassword()).getSingleResult());
    }
    public static User findUserByUsername(String username) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
