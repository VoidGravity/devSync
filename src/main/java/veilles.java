import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class veilles {
    public static void main(String[] args) {
        List<String> myList = new ArrayList<>();
        myList.add("Hello1");
        myList.add("Hello2");
        myList.add("Hello3");
        myList.add("Hello4");
        myList.add("Hello5");
//        for(String m : myList){
//            System.out.println(m);
////            myList.add("Hello6");
//        }
        CopyOnWriteArrayList<String> myL = new CopyOnWriteArrayList<>();
        myL.add("Hello1");
        myL.add("Hello2");
        myL.add("Hello3");
        Iterator<String> it=myL.iterator();
        while(it.hasNext()){
            String myString = it.next();
            System.out.println(myString);
            myL.add("Hello5");
        }
        System.out.println(myL);

    }
}
