package com.example.mapapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapapplication.R;
import com.example.mapapplication.adapter.PlacesAdapter;
import com.example.mapapplication.model.Place;
import com.example.mapapplication.util.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.mapapplication.util.Constants.*;
import static com.example.mapapplication.util.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public boolean mLocationPermissionGranted;
    private TextView tvSearch;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Place place;
    private ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationPermission();
        setBackButtonOnToolbar();
        initViews();
    }

    /**
     * initializing buttons , edit texts
     */
    private void initViews() {
        tvSearch=findViewById(R.id.tvSearch);
        btnSearch=findViewById(R.id.btnSearch);
        setOnClickListeners();
    }

    /**
     * setting on click listeners for auto complete text view and
     * search button.
     */
    private void setOnClickListeners() {
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSearchActivity();
            }
        });

    }

    /**
     * on click of textView and search button move to search activity.
     */
    private void navigateToSearchActivity() {
        Intent intent=new Intent(this,SearchActivity.class);
        startActivityForResult(intent, REQ_CODE_MAPS_TO_SEARCH);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, getString(R.string.map_ready) , Toast.LENGTH_SHORT).show();
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }
    /**
     * back button on click listener.
     */
    public void setBackButtonOnToolbar(){
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.location);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_CODE_MAPS_TO_SEARCH && data != null) {
            place=data.getParcelableExtra(EXTRA_PLACE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                try {
                    JSONObject jObject=new JSONObject(String.valueOf(place));
                    JSONArray results=jObject.getJSONArray("results");
                    for(int i=0;i<results.length();i++){

                        JSONObject location=results.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                        double lat=location.optDouble("lat");
                        double lng=location.optDouble("lng");
                        tvSearch.setText(place.getDescription());
                        setMarkerOnSelectedPlace(lat,lng);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMarkerOnSelectedPlace(double lat,double lng) {
        LatLng selectedPlace = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(selectedPlace).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedPlace));
    }
    /**
     * getting current device location.
     */
    private void getDeviceLocation(){
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionGranted){
                final Task location=mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation= (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),15f);
                        }
                        else{
                            Toast.makeText(MapsActivity.this,getString(R.string.unableCurrentLoc) ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch(SecurityException e){
            Log.d("-----------", getString(R.string.errorMessage) + e.getMessage());
        }
    }

    /**
     * to move camera.
     * @param latLng
     * @param v
     */
    private void moveCamera(LatLng latLng, float v) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,v));
    }
    /**
     * getting permission to fetch location.
     */
    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted=false;
        switch(requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted=true;
                }
            }
        }

    }
}
