/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package FriendlyPlanJava;

import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 *
 * @author dinhd
 */
public class FriendlyPlanJava {

    static int numWeek = getThisWeek();
    static ArrayList<Event> arE = new ArrayList<>();
    static ArrayList<LocalDate> beginWeeks = getBeginWeeks();
    static ArrayList<LocalDate> endWeeks = getEndWeeks();

    public static void main(String[] args) {
        boolean run = true;

        do {
            //Show calendar and menu
            showCalendarByWeek(arE, numWeek);
            int op = Validate.inputOption();
            //run menu
            run = runMenu(op, arE);
            System.out.println("\n\n\n");

        } while (run);
    }

    private static void showCalendarByWeek(ArrayList<Event> arE, int numOfWeek) {
        System.out.println("\nWeek " + (numOfWeek + 1) + " :" + beginWeeks.get(numOfWeek) + " -> " + endWeeks.get(numOfWeek));
        System.out.println("=========================================== Calendar =================================================\n"
                + "Mon\t\tTue\t\tWed\t\tThu\t\tFri\t\tSat\t\tSun");
//        ArrayList<Event> arE_now = getEventThisWeek(arE, numOfWeek);
        String[] dow = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        //sort hour
        for (int i = 0; i <= 23; i++) {
            boolean haveEvent = false;
            //run in week.
            for (String d : dow) {
                //run all event
                for (Event e : arE) {
                    //check in week
                    if (!e.getDate().isBefore(beginWeeks.get(numOfWeek)) && !e.getDate().isAfter(endWeeks.get(numOfWeek))) {

                        //ex:0h
                        if (e.getTime_start().getHour() == i) {
                            //ex:MONDAY
                            if (!d.equals("SUNDAY")) {

                                if (e.getDate().getDayOfWeek().toString().equals(d)) {
                                    System.out.print(i + "h:" + e.getId() + "." + e.getName() + "\t");
                                    haveEvent = true;
                                } else {
                                    System.out.print("\t\t");
                                }
                            } else {
                                if (e.getDate().getDayOfWeek().toString().equals(d)) {
                                    System.out.print(i + "h:" + e.getId() + "." + e.getName() + "\n");
                                } else if (haveEvent) {
                                    System.out.println("");
                                }

                            }

                        }
                    }
                }

            }
        }

        System.out.println("\n1.<- back week\t3.add event\t4.set event\t5.delete event\t2.next week ->\n"
                + "\t\t6.todos box\t7.eisen box\t8.Exit");
        System.out.print("Choose a number :");
    }

    private static boolean runMenu(int op, ArrayList<Event> arE) {
        switch (op) {
            case 3:
                arE = addEvent(arE);
                break;
            case 4:
                String id = showSetEvent();
                arE = setEvent(id, arE);
                break;
            case 5:
                id = showDelEvent();
                arE = delEvent(id, arE);
            case 1:
                if (numWeek != 0) {
                    numWeek = numWeek - 1;
                }

                break;
            case 2:
                if (numWeek != 4) {
                    numWeek = numWeek + 1;
                }
                break;
            case 6:
                arE = toDoBox(arE);
                arE = setNotDoneEvent(arE);
                break;
            case 7:
                eisenBox(arE);
                break;
            case 8:
                return false;
            default:
        }
        return true;
    }

    private static ArrayList<LocalDate> getEndWeeks() {
        LocalDate today = LocalDate.now();
        //Ngay dau và ngay cuoi cac tuan trong thang hien tai
        ArrayList<LocalDate> ldWs_Weeks = new ArrayList<>();
        Month month = today.getMonth();
        int year = today.getYear();
        //Ngay dau tien trong thang hien tai
        LocalDate fd = LocalDate.of(year, month, 1);
        LocalDate ld = LocalDate.of(year, month, maxDay(today.getMonthValue(), year));
        //them dau tuan va cuoi tuan
        LocalDate runEndWeek = fd;
        for (int i = 0; i < 4;) {
            //find weekend
            while (!runEndWeek.getDayOfWeek().toString().equals("SUNDAY")) {
                runEndWeek = runEndWeek.plusDays(1);
                if (runEndWeek.isEqual(ld)) {
                    break;
                }
            }
            ldWs_Weeks.add(runEndWeek);
            runEndWeek = runEndWeek.plusDays(1);
            i++;
        }
        ldWs_Weeks.add(ld);
        return ldWs_Weeks;
    }

    private static ArrayList<LocalDate> getBeginWeeks() {
        //thang nao?
        LocalDate today = LocalDate.now();
        Month month = today.getMonth();
        int year = today.getYear();

        //cac ngay dau tuan trong thang hien tai
        ArrayList<LocalDate> fdWs_Weeks = new ArrayList<>();
        //Ngay dau tien trong thang hien tai
        LocalDate fd = LocalDate.of(year, month, 1);
        LocalDate ld = LocalDate.of(year, month, maxDay(today.getMonthValue(), year));
        //them dau tuan va cuoi tuan
        LocalDate runBeginWeek = fd;
        fdWs_Weeks.add(runBeginWeek);
        for (int i = 0; i < 4;) {
            //find weekend
            while (!runBeginWeek.getDayOfWeek().toString().equals("MONDAY")) {
                runBeginWeek = runBeginWeek.plusDays(1);
            }
            if (!runBeginWeek.isEqual(ld)) {
                fdWs_Weeks.add(runBeginWeek);
            }
            runBeginWeek = runBeginWeek.plusDays(1);
            i++;
        }
        return fdWs_Weeks;
    }

    public static int maxDay(int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

    private static int getThisWeek() {
        LocalDate today = LocalDate.now();
        int thisWeek = 0;
        ArrayList<LocalDate> beginWeeks = getBeginWeeks();
        ArrayList<LocalDate> endWeeks = getEndWeeks();
        for (int i = 0; i < endWeeks.size(); i++) {
            if (today.isBefore(endWeeks.get(i))) {
                thisWeek = i;
                break;
            }
        }
        return thisWeek;
    }

    private static ArrayList<Event> addEvent(ArrayList<Event> arE) {
        Scanner sc = new Scanner(System.in);
        System.out.println("---------- Add Event ----------");
        //String id,String name, LocalDate date_start, LocalDate date_end, LocalDateTime time_start, LocalDateTime time_end, int lv
        try {
            System.out.print("Enter id: ");
            String id = Validate.inputId(arE);
            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter date : \nDay :");
            int date_day = Validate.inputInt();
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), date_day);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("It is in the past");
                throw new Exception();
            }

            System.out.print("Enter start time : \nHour:");
            int time_start_hour = Validate.inputInt();
            System.out.print("Minute :");
            int time_start_minute = Validate.inputInt();
            LocalDateTime start_time = date.atTime(time_start_hour, time_start_minute);
            if (start_time.isBefore(LocalDateTime.now())) {
                System.out.println("It is in the past");
                throw new Exception();
            }
            if(checkDuplicateTime(start_time,arE)){
                System.out.println("This time have used for other event!");
                throw new Exception();
            }

            System.out.print("Enter end time : \nHour:");
            int time_end_hour = Validate.inputInt();
            System.out.print("Minute :");
            int time_end_minute = Validate.inputInt();
            LocalDateTime end_time = date.atTime(time_end_hour, time_end_minute);
            if (end_time.isBefore(start_time)) {
                throw new Exception();
            }

            int lv = 0;
            System.out.print("Is it important ? 0.No 1.Yes :");
            int im = Validate.inputInt();
            System.out.print("Is it urgent ? 0.No 1.Yes :");
            int ur = Validate.inputInt();
            if (im == 1 && ur == 1) {
                lv = 1;
            } else if (im == 1) {
                lv = 2;
            } else if (ur == 1) {
                lv = 3;
            } else {
                lv = 4;
            }

            Event e = new Event(id, name, date, start_time, end_time, lv);
            arE.add(e);
            System.out.println("Added!");
        } catch (Exception e) {
            System.out.println("Error!");
        }

        return arE;
    }

    private static ArrayList<Event> setEvent(String id, ArrayList<Event> arE) {
        Scanner sc = new Scanner(System.in);
        //String id,String name, LocalDate date_start, LocalDate date_end, LocalDateTime time_start, LocalDateTime time_end, int lv
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter date : \nDay :");
            int date_day = Validate.inputInt();
            LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), date_day);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("It is in the past");
                throw new Exception();
            }

            System.out.print("Enter start time : \nHour:");
            int time_start_hour = Validate.inputInt();
            System.out.print("Minute :");
            int time_start_minute = Validate.inputInt();
            LocalDateTime start_time = date.atTime(time_start_hour, time_start_minute);
            if (start_time.isBefore(LocalDateTime.now())) {
                System.out.println("It is in the past!");
                throw new Exception();
            }
            if(checkDuplicateTime(start_time, arE)){
                System.out.println("This time have used for other event!");
                throw new Exception();
            }

            System.out.print("Enter end time : \nHour:");
            int time_end_hour = Validate.inputInt();
            System.out.print("Minute :");
            int time_end_minute = Validate.inputInt();
            LocalDateTime end_time = date.atTime(time_end_hour, time_end_minute);
            if (end_time.isBefore(start_time)) {
                System.out.println("It must continue form start time");
                throw new Exception();
            }

            int lv = 0;
            System.out.print("Is it important ? 0.No 1.Yes :");
            int im = Validate.inputInt();
            System.out.print("Is it urgent ? 0.No 1.Yes :");
            int ur = Validate.inputInt();
            if (im == 1 && ur == 1) {
                lv = 1;
            } else if (im == 1) {
                lv = 2;
            } else if (ur == 1) {
                lv = 3;
            } else {
                lv = 4;
            }

            Event e = new Event(id, name, date, start_time, end_time, lv);
            System.out.println("Changed!");
        } catch (Exception e) {
        }

        return arE;

    }

    private static ArrayList<Event> toDoBox(ArrayList<Event> arE) {
        System.out.print("\n\n\n=========== List Event Today ==========\n");
        ArrayList<Event> arE_Today = new ArrayList<>();
        for (Event e : arE) {
            if (e.getDate().equals(LocalDate.now())) {
                System.out.println(e);
                arE_Today.add(e);
            }
        }
        if (!arE_Today.isEmpty()) {
            System.out.print("Enter number of finished events:");
            System.out.print("0.No 1.All 2.More:");
            int op = Validate.inputInt();
            switch (op) {
                case 2:
                    System.out.println("How many ? :");
                    int numOfFEvent = Validate.inputInt();
                    do {
                        System.out.print("Enter id finished events: ");
                        Scanner sc = new Scanner(System.in);
                        String id = sc.nextLine();
                        delEvent(id, arE);
                        numOfFEvent--;
                    } while (numOfFEvent != 0 && !arE_Today.isEmpty());
                    break;
                case 1:
                    for (Event e : arE) {
                        if (e.getDate().equals(LocalDate.now())) {
                            arE.remove(e);
                        }
                    }
                    break;
                case 0:
                    break;
            }
        } else {
            System.out.println("Nothing to do today !");
        }

        return arE;
    }

    private static ArrayList<Event> delEvent(String id, ArrayList<Event> arE) {
        for (int i = 0; i < arE.size(); i++) {
            if (id.equals(arE.get(i).getId())) {
                arE.remove(i);
                System.out.println("Deleted!");
                return arE;
            }
        }
        System.out.println("Not found to delete");
        return arE;
    }

    private static String showDelEvent() {
        System.out.println("\n\n\n============= Delete Event ===============");
        System.out.print("Enter id event to delete : ");
        Scanner sc = new Scanner(System.in);
        String id = sc.nextLine();
        return id;
    }

    private static ArrayList<Event> setNotDoneEvent(ArrayList<Event> arE) {
        for (int i = 0; i < arE.size(); i++) {
            if (arE.get(i).getDate().equals(LocalDate.now())) {
                System.out.print("Do you wanna do : " + arE.get(i).getId() + " " + arE.get(i).getName() + " in the future ? \n"
                        + "0.No and delete\t1.Yes :");
                int op = Validate.inputInt();
                if (op == 1) {
                    Scanner sc = new Scanner(System.in);
                    //String id,String name, LocalDate date_start, LocalDate date_end, LocalDateTime time_start, LocalDateTime time_end, int lv
                    try {
                        String name = arE.get(i).getName();

                        System.out.print("Enter date : \nDay :");
                        int date_day = Validate.inputInt();
                        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), date_day);
                        if (date.isBefore(LocalDate.now())) {
                            System.out.println("It is in the past");
                            throw new Exception();
                        }

                        System.out.print("Enter start time : \nHour:");
                        int time_start_hour = Validate.inputInt();
                        System.out.print("Minute :");
                        int time_start_minute = Validate.inputInt();
                        LocalDateTime start_time = date.atTime(time_start_hour, time_start_minute);
                        if (start_time.isBefore(LocalDateTime.now())||!checkDuplicateTime(start_time, arE)) {
                            System.out.println("It is in the past");
                            throw new Exception();
                        }

                        System.out.print("Enter end time : \nHour:");
                        int time_end_hour = Validate.inputInt();
                        System.out.print("Minute :");
                        int time_end_minute = Validate.inputInt();
                        LocalDateTime end_time = date.atTime(time_end_hour, time_end_minute);
                        if (end_time.isBefore(start_time)) {
                            System.out.println("It must continue form start time");
                            throw new Exception();
                        }

                        int lv = arE.get(i).getLv();

                        Event e = new Event(arE.get(i).getId(), name, date, start_time, end_time, lv);
                        System.out.println("Changed!");
                    } catch (Exception e) {
                    }
                } else {
                    delEvent(arE.get(i).getId(), arE);
                }
            }
        }
        return arE;
    }

    private static void eisenBox(ArrayList<Event> arE) {
        System.out.println("\n\n\n======================================== EISENHOWER MATRIX ==========================================");
        arE = sortArrayByLevel(arE);
        ArrayList<Event> b1 = new ArrayList<Event>(), b2 = new ArrayList<Event>(), b3 = new ArrayList<Event>(), b4 = new ArrayList<Event>();
        for (Event e : arE) {
            if (!e.getDate().isBefore(beginWeeks.get(numWeek)) && !e.getDate().isAfter(endWeeks.get(numWeek))) {
                switch (e.getLv()) {
                    case 1:
                        b1.add(e);
                        break;
                    case 2:
                        b2.add(e);
                        break;
                    case 3:
                        b3.add(e);
                        break;
                    case 4:
                        b4.add(e);
                        break;
                }
            }

        }

        System.out.print("\t\t|\t\tUrgent\t\t\t|\t\tNot Urgent\t\t|\n");
        System.out.print("Important\t|");

        if (b1.size() == b2.size()) {
            for (int i = 0; i < b1.size() && !b1.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t|%s\tid  %s:%s\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName(), b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());
                } else {
                    System.out.printf("\t\t|%s\tid  %s:%s\t\t|%s\tid  %s:%s\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName(), b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());
                }
            }
        } else if (b1.size() < b2.size()) {
            for (int i = 0; i < b1.size() && !b1.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t||%s\tid  %s:%s\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName(), b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());
                } else {
                    System.out.printf("\t\t|%s\tid  %s:%s\t\t||%s\tid  %s:%s\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName(), b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());
                }
            }
            for (int i = b1.size(); i < b2.size() && !b2.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("\t\t\t\t\t|%s\tid  %s:%s\t\t|\n", b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());
                } else {
                    System.out.printf("\t\t|\t\t\t\t\t|%s\tid  %s:%s\t\t|\n", b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());
                }
            }
        } else if (b1.size() > b2.size()) {
            for (int i = 0; i < b2.size() && !b2.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t|%s\tid  %s:%s\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName(), b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());

                } else {

                    System.out.printf("\t\t|%s\tid  %s:%s\t\t\t|%s\tid  %s:%s\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName(), b2.get(i).getDate(), b2.get(i).getId(), b2.get(i).getName());

                }
            }
            for (int i = b2.size(); i < b1.size(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t|\t\t\t\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName());
                } else {
                    System.out.printf("\t\t|\tid:%s %s %s\t|\t\t\t\t\t|\n", b1.get(i).getDate(), b1.get(i).getId(), b1.get(i).getName());
                }
            }
        }
        System.out.print("\nNot Important\t|");
        if (b3.size() == b4.size()) {
            for (int i = 0; i < b3.size() && !b3.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t|%s\tid  %s:%s\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName(), b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());
                } else {
                    System.out.printf("\t\t|%s\tid  %s:%s\t\t|%s\tid  %s:%s\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName(), b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());
                }
            }
        } else if (b3.size() < b4.size()) {
            for (int i = 0; i < b3.size() && !b3.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t||%s\tid  %s:%s\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName(), b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());
                } else {
                    System.out.printf("\t\t|%s\tid  %s:%s\t\t||%s\tid  %s:%s\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName(), b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());
                }
            }
            for (int i = b3.size(); i < b4.size() && !b4.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("\t\t\t\t\t|%s\tid  %s:%s\t\t|\n", b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());
                } else {
                    System.out.printf("\t\t|\t\t\t\t\t|%s\tid  %s:%s\t\t|\n", b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());
                }
            }
        } else if (b3.size() > b4.size()) {
            for (int i = 0; i < b4.size() && !b4.isEmpty(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t|%s\tid  %s:%s\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName(), b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());

                } else {

                    System.out.printf("\t\t|%s\tid  %s:%s\t\t\t|%s\tid  %s:%s\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName(), b4.get(i).getDate(), b4.get(i).getId(), b4.get(i).getName());

                }
            }
            for (int i = b4.size(); i < b3.size(); i++) {
                if (i == 0) {
                    System.out.printf("%s\tid  %s:%s\t\t|\t\t\t\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName());
                } else {
                    System.out.printf("\t\t|\tid:%s %s %s\t|\t\t\t\t\t|\n", b3.get(i).getDate(), b3.get(i).getId(), b3.get(i).getName());
                }
            }
        }
    }

    private static ArrayList<Event> sortArrayByLevel(ArrayList<Event> arE) {
        arE.sort((o1, o2) -> {
            if (o1.getLv() > o2.getLv()) {
                return 1;
            }
            return 0; // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/LambdaBody
        });

        return arE;
    }

    private static String showSetEvent() {
        System.out.println("---------- Set Event ----------");
        System.out.print("Enter id: ");
        String id = Validate.inputId(arE);
        return id;
    }

    private static boolean checkDuplicateTime(LocalDateTime t ,ArrayList<Event> arE) {
        for(Event e :arE){
             if(e.getTime_start().equals(t)){
                 return true;
             }
        }
        return false;
    }
}
