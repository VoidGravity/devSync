// this is a file to test stuff and learn
// curently testing EMF EM Optimisation

import javax.persistence.*;

public class EMF {
    public static void main(String[] args) {


        long curent = System.currentTimeMillis();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("DevSyncPU");

        EntityManager em = emf.createEntityManager();
        em.close();

//         emf.close();

        long end = System.currentTimeMillis();
        System.out.println("curent time : "+( end - curent));

    }
}