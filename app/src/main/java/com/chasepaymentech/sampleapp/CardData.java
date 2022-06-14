package com.chasepaymentech.sampleapp;

public class CardData {

    private String pan;
    private String cvv;


    public CardData(String pan, String cvv) {
        this.pan = pan;
        this.cvv = cvv;
    }

    public String getPan() {
        return pan;
    }

    public String getCvv() {
        return cvv;
    }

}
