package com.example.leidosrollvan.dataClasses;

import java.util.ArrayList;
import java.util.HashMap;
public class BusinessMenu extends Business {

    //key can be menuSection
   // private ArrayList<String> categories;//Eg. Asian food, Indian etc.
   // private HashMap<String, Food> Menu = new HashMap<String, Food>();
    //private ArrayList<String> menuSection ;//eg. breakfast, beverages, snacks etc

    public HashMap<String, ArrayList<HashMap<String, String>>> businessMenuItems = new HashMap<String, ArrayList<HashMap<String, String>>>();
    public ArrayList<String> categories = new ArrayList<String>();

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

    public ArrayList<String> getSections(){
        return new ArrayList<String>(this.businessMenuItems.keySet());
    }

    public boolean isEmptyMenu(){
        return this.businessMenuItems.isEmpty();
    }
    
}



