package com.gpd.gpdimg.activity;


import java.io.Serializable;

public class PreIgnitionCheckedListModel implements Serializable{


    private String image_name;

    private String image_selection_value;



    public String getImage_Name() {
        return image_name;
    }

    public void setImage_Name(String  image_name) {
        this.image_name = image_name;
    }

    public String getImage_SelectionValue() {
        return image_selection_value;
    }

    public void setImage_SelectionValue(String image_selection_value) {
        this.image_selection_value = image_selection_value;
    }

}
