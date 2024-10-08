package com.devsync.web;

import com.devsync.model.User;
import com.devsync.util.JPAutil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.*;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/user")

public class WebFilterSevlet implements Filter {

    EntityManagerFactory emf = JPAutil.EMF();
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {


            String user = req.getParameter("username").trim();
            String password = req.getParameter("password").trim();
            User myuser = new User();
            myuser.setUsername(user);
            myuser.setPassword(password);
            EntityManager em = emf.createEntityManager();
            String sql = "select u from User u where u.username = :username and u.password = :password ";
        try{

            em.createQuery(sql, User.class).setParameter("username",myuser.getUsername()).setParameter("password",myuser.getPassword()).getSingleResult();
            chain.doFilter(req,resp);


        } catch (Exception e) {
            HttpServletResponse response = (HttpServletResponse) resp;
            response.sendRedirect("/");
//            req.getRequestDispatcher("/")
            System.out.println("User not found");
            e.printStackTrace();

        }finally {
            em.close();
        }
    }
}
