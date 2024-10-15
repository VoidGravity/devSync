package com.devsync.dao;

import com.devsync.model.Task;
import com.devsync.model.User;
import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.Date;
import java.util.List;


public class TaskDAO {

    public static void create(Task task) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(task);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static Task findTask(Long id) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            return em.find(Task.class, id);
        } finally {
            em.close();
        }
    }


    public static void updateTask(Task task) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(task);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static void deleteTask(Task task) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            em.getTransaction().begin();
            Task managedTask = em.merge(task);
            em.remove(managedTask);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public static List<Task> getOverdueTasks() {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "SELECT t FROM Task t WHERE t.dueDate < :currentDate AND t.completed = false", Task.class);
            query.setParameter("currentDate", new Date());
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    public static List<Task> getTasksForUser(User user) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.tags WHERE t.assignedTo = :user", Task.class);
            query.setParameter("user", user);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}