package com.example.mapapp.ui.map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("place_name")
    @Expose
    private String name;
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;



    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }


    public Double getLatitude() {
        return geometry.getCoordinates().get(1);
    }

    public Double getLongtitude() {
        return geometry.getCoordinates().get(0);
    }

    public class Geometry {
        @SerializedName("coordinates")
        @Expose
        private List<Double> coordinates = null;

        public List<Double> getCoordinates() {
            return coordinates;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setName(String name) {
        this.name = name;
    }


}
