package com.example.mapapp.ui.bookmark;

public class BookmarkItem {
    private String name;
    private double latitude;
    private double longtitude;

    public BookmarkItem(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }
}
