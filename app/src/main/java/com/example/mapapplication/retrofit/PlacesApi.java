package com.example.mapapplication.retrofit;

import android.gesture.Prediction;

import com.example.mapapplication.model.Place;
import com.example.mapapplication.model.PredictionResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * places api for retrofit api hit query.
 */
public interface PlacesApi {
    String QUERY_API="api/place/autocomplete/json?types=address={input}&key=AIzaSyAN7K_l1Rp1oe8H8vZdgeeQ2_Koz4qqB8g";
    String QUERY_PLACE="api/place/details/json?placeid={placeId}&fields=&key=AIzaSyAN7K_l1Rp1oe8H8vZdgeeQ2_Koz4qqB8g";
    /**
     * query appends the parameter
     */
    @GET(QUERY_API)
    Call <PredictionResponse> loadPredictions(@Query("input") String address);

    @GET(QUERY_PLACE)
    Call <Place> getPlace(@Query("placeId") String placeId);

}
