package com.example.mapapplication.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mapapplication.R;
import com.example.mapapplication.adapter.PlacesAdapter;
import com.example.mapapplication.model.Place;
import com.example.mapapplication.model.PredictionResponse;
import com.example.mapapplication.retrofit.PlacesApi;
import com.example.mapapplication.retrofit.RetrofitClient;
import com.example.mapapplication.util.Constants;
import com.example.mapapplication.util.RvListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, RvListener {

    private ImageButton ibBack, ibCancel;
    private EditText etSearch;
    private RecyclerView recPlaces;
    private PlacesAdapter placesAdapter;
    private ArrayList<Place> placeArrayList;
    private Place place;
    private String placeId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
        placeArrayList=new ArrayList<>();
    }

    /**
     * initializing views for search activity.
     */
    private void initViews() {
        ibBack=findViewById(R.id.ibBack);
        ibBack.setOnClickListener(this);
        ibCancel=findViewById(R.id.ibCancel);
        ibCancel.setOnClickListener(this);
        etSearch=findViewById(R.id.etSearch);
        etSearch.setOnClickListener(this);
        initRv();
    }

    /**
     * initializing recyclerView
     */
    private void initRv() {
        recPlaces=findViewById(R.id.recPlaces);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recPlaces.setLayoutManager(layoutManager);

//        placesAdapter=new PlacesAdapter(placeArrayList,SearchActivity.this);
//        recPlaces.setAdapter(placesAdapter);

    }

    /**
     * setting on click listeners on menu buttons and edit text.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.ibBack:
                finish();
                break;
            case R.id.ibCancel:
                etSearch.setText("");
                break;
            case R.id.etSearch:
                handleSearchSuggestions();
                break;
        }
    }

    /**
     * handling suggestions and hitting api on text watcher click.
     */
    private void handleSearchSuggestions() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesApiCall();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    /**
     * hitting api for auto completion of places in auto complete text view.
     */
    private void placesApiCall() {
        Retrofit retrofit= RetrofitClient.getApiService();
        final PlacesApi placesApi=retrofit.create(PlacesApi.class);

        Call <PredictionResponse> call= placesApi.loadPredictions(etSearch.getText().toString());
        call.enqueue(new Callback <PredictionResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call <PredictionResponse> call, Response <PredictionResponse> response) {
                if(response.isSuccessful()) {
                    PredictionResponse predictionResponse = response.body();
                    placeArrayList=predictionResponse.getPredictionList();

                    placesAdapter=new PlacesAdapter(placeArrayList,SearchActivity.this);
                    recPlaces.setAdapter(placesAdapter);
                    Log.e("------------", "onResponse: "+placeArrayList.get(2).getDescription() );

//                    placesAdapter.updatePlaces(placeArrayList);
                }

            }

            @Override
            public void onFailure(Call <PredictionResponse> call, Throwable t) {
                Toast.makeText(SearchActivity.this,getString(R.string.errormsg),Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * api call to fetch place id to send call back to previous activity.
     */
    private void selectedPlaceApiCall(String placeId) {

        Retrofit retrofit= RetrofitClient.getApiService();
        final PlacesApi placesApi=retrofit.create(PlacesApi.class);
        Call<Place> call=placesApi.getPlace(placeId);
        call.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                if(response.isSuccessful()) {
                    place=response.body();
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                Toast.makeText(SearchActivity.this,getString(R.string.error),Toast.LENGTH_SHORT).show();
            }
        });
        navigateToMapsActivity();

    }

    @Override
    public void onRvItemClick(int position) {
        getPlaceIdofSelectedPlace(position);
    }

    /**
     * getting place id from response of selected place.
     * @param position
     */
    private void getPlaceIdofSelectedPlace(int position) {
        placeId=placeArrayList.get(position).getPlaceId();
        selectedPlaceApiCall(placeId);

    }

    /**
     * move back to maps activity with the place name.
     */
    public void navigateToMapsActivity(){
        Intent intent=new Intent();
        intent.putExtra(Constants.EXTRA_PLACE,place);
        setResult(RESULT_OK,intent);
        finish();
    }
}
