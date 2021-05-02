package com.example.mapapp.ui.map;

public class Location {
    private double latitude;
    private double longitude;
    private String text;
    private String name;
    private String type;

    public Location(double latitude, double longitude, String text, String name, String type) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.text = text;
        this.name = name;
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
