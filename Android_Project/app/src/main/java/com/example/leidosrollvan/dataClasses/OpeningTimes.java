package com.example.leidosrollvan.dataClasses;

import android.util.Log;

import java.lang.reflect.Array;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.opencensus.internal.StringUtils;

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

    @Override
    public String toString() {
        String openingTime = Integer.toString(this.hourOpening)+":"+String.format("%02d", this.minuteOpening);
        String closingTime = Integer.toString(this.hourClosing)+":"+Integer.toString(this.minuteClosing);
        String OH = String.format("Open from %s to %s every:",openingTime,closingTime);
//        ArrayList<String> sortedDOW = new ArrayList<String>(Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"));
//        sortedDOW.retainAll(DaysOfWeek);
//        ArrayList<String> DOW = new ArrayList<String>();
//        for(String day:sortedDOW){
//            DOW.add(day.substring(0,3));
//        }
//        String OD = "Days open: "+ join(",",DOW);
        return OH;
    }

    private static String join(String separator, ArrayList<String> input) {
        if (input == null || input.size() <= 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.size(); i++) {
            sb.append(input.get(i));
            // if not the last item
            if (i != input.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();

    }
}
