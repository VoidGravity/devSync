package com.devsync.dao;

import com.devsync.model.Task;
import com.devsync.model.User;
import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskDAO {
    private static final Logger logger = Logger.getLogger(TaskDAO.class.getName());

    public static void createTask(Task task) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(task);
            em.getTransaction().commit();
            logger.info("Task created successfully with ID: " + task.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.log(Level.SEVERE, "Error creating task", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public static Task findTask(Long id) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            Task task = em.find(Task.class, id);
            if (task != null) {
                task.getTags().size(); // Initialize tags
            }
            return task;
        } finally {
            em.close();
        }
    }

    public static List<Task> getTasksForUser(User user) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.tags WHERE t.assignedTo.id = :userId ORDER BY t.dueDate",
                    Task.class
            );
            query.setParameter("userId", user.getId());
            return query.getResultList();
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
            logger.info("Task updated successfully with ID: " + task.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.log(Level.SEVERE, "Error updating task", e);
            throw e;
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
            logger.info("Task deleted successfully with ID: " + task.getId());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.log(Level.SEVERE, "Error deleting task", e);
            throw e;
        } finally {
            em.close();
        }
    }

    public static List<Task> getTasksForManagerDashboard(User manager, String timeFrame, String tagName) {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            StringBuilder queryString = new StringBuilder(
                    "SELECT DISTINCT t FROM Task t LEFT JOIN FETCH t.tags tag WHERE 1=1 "
            );

            // Add manager condition
            queryString.append("AND t.createdBy.id = :managerId ");

            // Add tag filter if provided
            if (tagName != null && !tagName.isEmpty()) {
                queryString.append("AND tag.name = :tagName ");
            }

            // Add time frame filter
            if (timeFrame != null && !timeFrame.isEmpty()) {
                queryString.append("AND t.creationDate >= :startDate ");
            }

            TypedQuery<Task> query = em.createQuery(queryString.toString(), Task.class);
            query.setParameter("managerId", manager.getId());

            // Set tag parameter if needed
            if (tagName != null && !tagName.isEmpty()) {
                query.setParameter("tagName", tagName);
            }

            // Set time frame parameter if needed
            if (timeFrame != null && !timeFrame.isEmpty()) {
                Date currentDate = new Date();
                Date startDate;

                switch (timeFrame) {
                    case "week":
                        startDate = new Date(currentDate.getTime() - 7L * 24 * 60 * 60 * 1000);
                        break;
                    case "month":
                        startDate = new Date(currentDate.getTime() - 30L * 24 * 60 * 60 * 1000);
                        break;
                    case "year":
                        startDate = new Date(currentDate.getTime() - 365L * 24 * 60 * 60 * 1000);
                        break;
                    default:
                        startDate = new Date(currentDate.getTime() - 30L * 24 * 60 * 60 * 1000);
                }

                query.setParameter("startDate", startDate);
            }

            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error fetching tasks for manager dashboard", e);
            throw e;
        } finally {
            em.close();
        }
    }
    public static List<Task> getOverdueTasks() {
        EntityManager em = JPAutil.EMF().createEntityManager();
        try {
            TypedQuery<Task> query = em.createQuery(
                    "SELECT t FROM Task t " +
                            "WHERE t.dueDate < :currentDate " +
                            "AND t.completed = false",
                    Task.class
            );
            query.setParameter("currentDate", new Date());
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}