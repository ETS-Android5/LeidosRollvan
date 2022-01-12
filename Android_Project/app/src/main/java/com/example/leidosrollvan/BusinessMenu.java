package com.example.leidosrollvan;
import android.widget.ArrayAdapter;

import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
public class BusinessMenu {


    //Key is category.
    private HashMap<String, HashMap<String, Double>> Menu = new HashMap<String, HashMap<String, Double>>();

    public BusinessMenu(HashMap<String, HashMap<String, Double>> Menu){
        this.Menu = Menu;
    }


    public HashMap<String, HashMap<String, Double>> getMenu() {
        return Menu;
    }

    public void setMenu(HashMap<String, HashMap<String, Double>> menu) {
        Menu = menu;
    }

    public int getsize(){
        int count = 0;
        for(HashMap<String,Double> values:Menu.values()){
            count+=values.size();
        }
        return count;
    }
}


