package com.devsync.util;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
@Singleton
public class JPAutil {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory EMF(){
       if(emf==null){
           emf = Persistence.createEntityManagerFactory("default");
       }
       return emf;

    }
    public static void EMFclose(){
        emf.close();
    }


}
