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

    public static List<Task> getTasksForManagerDashboard(User manager, String timeFrame, String tagName) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            StringBuilder queryString = new StringBuilder("SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.tags tag WHERE t.createdBy = :manager ");

            if (tagName != null && !tagName.isEmpty()) {
                queryString.append("AND tag.name = :tagName ");
            }

            if (timeFrame != null && !timeFrame.isEmpty()) {
                queryString.append("AND t.creationDate >= :startDate ");
            }

            TypedQuery<Task> query = em.createQuery(queryString.toString(), Task.class);
            query.setParameter("manager", manager);

            if (tagName != null && !tagName.isEmpty()) {
                query.setParameter("tagName", tagName);
            }

            if (timeFrame != null && !timeFrame.isEmpty()) {
                Date currentDate = new Date();
                Date startDate;

                if ("week".equals(timeFrame)) {
                    startDate = new Date(currentDate.getTime() - 7 * 24 * 60 * 60 * 1000L);
                } else if ("month".equals(timeFrame)) {
                    startDate = new Date(currentDate.getTime() - 30L * 24 * 60 * 60 * 1000);
                } else if ("year".equals(timeFrame)) {
                    startDate = new Date(currentDate.getTime() - 365L * 24 * 60 * 60 * 1000);
                } else {
                    // Default to last 30 days if an invalid timeFrame is provided
                    startDate = new Date(currentDate.getTime() - 30L * 24 * 60 * 60 * 1000);
                }

                query.setParameter("startDate", startDate);
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}