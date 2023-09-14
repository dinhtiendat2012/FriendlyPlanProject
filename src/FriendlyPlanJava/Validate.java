/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FriendlyPlanJava;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author dinhd
 */
public class Validate {

    static int inputInt() {
        try {
            Scanner sc = new Scanner(System.in);
            String num = sc.nextLine();
            return Integer.parseInt(num);
        } catch (Exception e) {
            System.out.println("It must be an integer. Try again !");
        }
        return inputInt();
    }

    static int inputOption() {
        return inputInt();
    }

    static String inputId(ArrayList<Event> arE) {

        do {
            try {
                Scanner sc = new Scanner(System.in);
                String id = sc.next();
                for (Event e : arE ) {
                    if (id.equals(e.getId())) {
                        throw new Exception();
                    }
                }
                return id;
            } catch (Exception e) {
            }
        } while (true);
    }
}
