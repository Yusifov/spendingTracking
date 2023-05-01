package com.example.spending_tracker.sqlite;

import java.text.AttributedString;

public class object {
    String place;
    float price;
    String time;

    object(){}
    public object(String place, float price, String time) {
        this.place = place;
        this.price = price;
        this.time = time;
    }

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getPlace() {
        return place;
    }
    public void setPlace(String place) {
        this.place = place;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String toString() {
        return place + " ( " + price + " )" + "[ " + time + " ]";
    }
}


