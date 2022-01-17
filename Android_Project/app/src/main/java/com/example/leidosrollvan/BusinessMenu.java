package com.example.leidosrollvan;

import java.lang.reflect.Array;
import java.util.ArrayList; // import the ArrayList class
import java.util.HashMap;
public class BusinessMenu {

    //key can be menuSection
   // private ArrayList<String> categories;//Eg. Asian food, Indian etc.
   // private HashMap<String, Food> Menu = new HashMap<String, Food>();
    //private ArrayList<String> menuSection ;//eg. breakfast, beverages, snacks etc

    private HashMap<String, ArrayList<HashMap<String, String>>> businessMenuItems;
    private ArrayList<String> categories;
    /*public BusinessMenu( ArrayList<String> categories, HashMap<String, Food> Menu){
        this.Menu = Menu;
        //this.menuSection = menuSection;
        this.categories = categories;
    }*/
    public BusinessMenu(){}

    public BusinessMenu(HashMap<String, ArrayList<HashMap<String, String>>> businessMenuItems,ArrayList<String> categories) {
        this.businessMenuItems = businessMenuItems;
        this.categories = categories;
    }





    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    //Checks for duplicates
    public void addCategories(String category){
        if(!this.categories.contains(category)){
            this.categories.add(category);
        }
    }

    //    private HashMap<String, ArrayList<HashMap<String, String>>> businessMenuItems;
    public void addMenuItems(String Section, HashMap<String,String> businessMenuItems){
        if(this.businessMenuItems.get(Section)==null){
            ArrayList<HashMap<String,String>> arr = new ArrayList<HashMap<String,String>>();
            arr.add(businessMenuItems);
            this.businessMenuItems.put(Section,arr);
        }
        else{
            this.businessMenuItems.get(Section).add(businessMenuItems);
        }

    }

    public HashMap<String, ArrayList<HashMap<String, String>>> getBusinessMenuItems() {
        return businessMenuItems;
    }

    public void setBusinessMenuItems(HashMap<String, ArrayList<HashMap<String, String>>> businessMenuItems) {
        this.businessMenuItems = businessMenuItems;
    }

    public void removeCategory(String Category){
        this.categories.remove(Category);
    }

    public void removeBusinessMenuItems(String Section, HashMap<String,String> businessMenuItems){
        this.businessMenuItems.get(Section).remove(businessMenuItems);
    }
}


