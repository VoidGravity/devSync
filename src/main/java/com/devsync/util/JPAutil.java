package com.devsync.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAutil {
    private static EntityManagerFactory emf;
    
    public static EntityManagerFactory EMF(){
       if(emf==null){
           emf = Persistence.createEntityManagerFactory("DevSyncPU");
       }
       return emf;

    }
    public static void EMFclose(){
        emf.close();
    }


}
