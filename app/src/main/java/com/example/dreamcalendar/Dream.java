package com.example.dreamcalendar;

public class Dream {
    String value;
    String type;
    String date;
    String color;

    public Dream(String value, String type, String date, String color) {
        this.value = value;
        this.type = type;
        this.date = date;
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}