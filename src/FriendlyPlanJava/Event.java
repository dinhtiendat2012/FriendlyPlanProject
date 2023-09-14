/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FriendlyPlanJava;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author dinhd
 */
public class Event {
    String id;
    String name;
    LocalDate date;
    LocalDateTime time_start,time_end;
    int lv;
    String des;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Event(String id,String name, LocalDate date, LocalDateTime time_start, LocalDateTime time_end, int lv) {
        this.id = id;
        this.name = name;
        this.date= date;
        this.time_start = time_start;
        this.time_end = time_end;
        this.lv = lv;
    }

    public Event() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getTime_start() {
        return time_start;
    }

    public void setTime_start(LocalDateTime time_start) {
        this.time_start = time_start;
    }

    public LocalDateTime getTime_end() {
        return time_end;
    }

    public void setTime_end(LocalDateTime time_end) {
        this.time_end = time_end;
    }

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    @Override
    public String toString() {
        return "ID "+id+": " + name + " ,in " + date + " ,at" + time_start.getHour()+":"+time_start.getMinute()+ " - " + time_end.getHour() +":"+time_end.getMinute()+ ",level : " + lv ;
    }
    
}
