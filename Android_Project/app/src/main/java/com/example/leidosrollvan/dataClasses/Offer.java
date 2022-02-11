package com.example.leidosrollvan.dataClasses;

import java.util.ArrayList;

public class Offer {
    public ArrayList<String> offers=new ArrayList<>();

    public Offer(){}

    public Offer(ArrayList<String> offers) {
        this.offers = offers;
    }

    public void addOffer(String offer){
        offers.add(offer);
    }

    public ArrayList<String> getOffers() {
        return offers;
    }
}
