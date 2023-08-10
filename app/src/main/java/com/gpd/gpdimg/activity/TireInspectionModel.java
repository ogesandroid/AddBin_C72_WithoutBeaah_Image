package com.gpd.gpdimg.activity;


import java.io.Serializable;

public class TireInspectionModel implements Serializable{


    private String tire_selection_number;

    public String getTire_SelectionNumber() {
        return tire_selection_number;
    }

    public void setTire_SelectionNumber(String tire_selection_number) {
        this.tire_selection_number = tire_selection_number;
    }

}
