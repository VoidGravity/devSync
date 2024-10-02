package com.devsync.config;

import com.devsync.util.JPAutil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TestWebListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAutil.EMFclose();
        System.out.println("CLOSE");
    }
}
