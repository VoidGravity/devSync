import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class EMF {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        String output = "";

                for(int i = input.length()-1; i>0; i--) {
                    output = input.charAt(i) +" "+ output;
                }
        System.out.println(input.length()+output);

    }
}