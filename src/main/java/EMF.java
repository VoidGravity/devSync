// this is a file to test stuff and learn
// curently testing EMF EM Optimisation


import com.devsync.dao.UserDAO;
import com.devsync.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class EMF {
    public static void main(String[] args) {
        UserDAO dao = new UserDAO();

            List<User> users = dao.getUsers();
        for(User user:users){
            System.out.println(user.getUsername());
        }
    }
//    public static void main(String[] args) {
//        Test1();
//        System.out.println("=======================================");
//        Test2();
//
//    }
    public static void Test1(){
        System.out.println("TEst1 : not closing EMF");
        long start = System.currentTimeMillis();
        for(int i=0;i<5;i++){
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("DevSyncPU");
         emf.createEntityManager();

        }
        long end = System.currentTimeMillis();

        System.out.println("TEst1 Result :" +(end-start));

    }
    public static void Test2(){
        System.out.println("TEst2 : closing EMF");
        long start = System.currentTimeMillis();
        for(int i=0;i<5;i++){
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("DevSyncPU");
         emf.createEntityManager();
         emf.close();
        }
        long end = System.currentTimeMillis();

        System.out.println("TEst2 Result :" +(end-start));

    }
}