package com.example.mapapplication.model;

import android.gesture.Prediction;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * the two main fields are predictions and status.
 */
public class PredictionResponse {
    @SerializedName("predictions")
    private ArrayList<Place> predictionList=null;

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Place> getPredictionList() {
        return predictionList;
    }

    public void setPredictionList(ArrayList<Place> predictionList) {
        this.predictionList = predictionList;
    }
}
