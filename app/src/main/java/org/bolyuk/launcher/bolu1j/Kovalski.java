package org.bolyuk.launcher.bolu1j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Kovalski {
    public static void l(String s){
        System.out.println(s);
    }

    public static String r(){
        try {
            Scanner in = new Scanner(System.in);

           return in.nextLine();
        }catch (Exception e){
            return e.toString();
        }
    }
}
