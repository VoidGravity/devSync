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
I will send you the following files (Testweblistenr?java , tagdao,taskdao,userdao,Tag,Task,User,schedulerService,tagservice,taskservice,userservice,managerdashboardservlet,taskservlet,createtask,listtasks,managerdashboard,updatetask), since I can only send them 5 by 5 you need wait till you recieve all of them , so don't start unless i send the following comand /start-generating
I have but I won't send you since they work already (roleenum,editUser.jsp,createUser.jsp,home.jsp,auth.jsp,webFIlterServlet,jpautil, testservlet(for user) authservelt,mainservlet(for auth related reaisons)) if you need anyfiles let me know , otherwise wait for the command