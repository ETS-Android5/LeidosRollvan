package com.example.leidosrollvan.dataClasses;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OpeningTimes {
    int hourOpening;
    int minuteOpening;
    int hourClosing;
    int minuteClosing;
    ArrayList<String> DaysOfWeek = new ArrayList<String>();

    public OpeningTimes(){};
    public OpeningTimes(int hourOpening, int minuteOpening, int hourClosing, int minuteClosing) {
        this.hourOpening = hourOpening;
        this.minuteOpening = minuteOpening;
        this.hourClosing = hourClosing;
        this.minuteClosing = minuteClosing;
    }

    public int getHourOpening() {
        return hourOpening;
    }

    public void setHourOpening(int hourOpening) {
        this.hourOpening = hourOpening;
    }

    public int getMinuteOpening() {
        return minuteOpening;
    }

    public void setMinuteOpening(int minuteOpening) {
        this.minuteOpening = minuteOpening;
    }

    public int getHourClosing() {
        return hourClosing;
    }

    public void setHourClosing(int hourClosing) {
        this.hourClosing = hourClosing;
    }

    public int getMinuteClosing() {
        return minuteClosing;
    }

    public void setMinuteClosing(int minuteClosing) {
        this.minuteClosing = minuteClosing;
    }

    public void addDaysOfWeek(String day){
        if(!DaysOfWeek.contains(day)){
            DaysOfWeek.add(day);
        }
    }

    public ArrayList<String> getDaysOfWeek() {
        return DaysOfWeek;
    }

    public void setDaysOfWeek(ArrayList<String> daysOfWeek) {
        DaysOfWeek = daysOfWeek;
    }

}
