package com.devsync.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class TestWebListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("init");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("CLOSE");
    }
}
