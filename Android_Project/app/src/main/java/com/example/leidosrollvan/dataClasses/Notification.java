package com.example.leidosrollvan.dataClasses;

import java.util.ArrayList;

public class Notification {
    public ArrayList<String> notis=new ArrayList<>();

    public Notification(){};

    public Notification(ArrayList<String> notis) {this.notis = notis; }

    public void addNoti(String noti){
            notis.add(noti);
        };

    public void deleteNoti(String noti){notis.remove(noti);}

    public ArrayList<String> getNotis() {
            return notis;
        }
}

