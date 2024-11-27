package com.example.crm;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class IndividualLocationActivity extends AppCompatActivity implements OnSuccessListener,
        OnMapReadyCallback {
    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextView locationNameTextView;
    private TextView descriptionTextView;
    private TextView accountNameTextView;
    private MapView mapView;
    private GoogleMap googleMap;
    private String mLatitude;
    private String mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_individual_location);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Location");

        locationNameTextView = (TextView) findViewById(R.id.locationNameTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        accountNameTextView = (TextView) findViewById(R.id.accountNameTextView);

        String locationId = getIntent().getStringExtra("locationId");

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        NetworkUtils.
                fetchDataGet("location/"+locationId,
                        "", IndividualLocationActivity.this,
                        IndividualLocationActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSuccess(ResponseBody responseBody) {
        String responseBodyString = null;
        try {
            String result = responseBody.string();
            System.out.println("Result: "+result);

            if (result != null && result.contains("location")){

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("location") &&
                        jsonObject.get("location") instanceof JSONObject
                ) {

                    JSONObject location = jsonObject.getJSONObject("location");

                    locationNameTextView.setText(location.getString("name"));
                    descriptionTextView.setText(location.getString("description"));
                    accountNameTextView.setText(location.getJSONObject("account")
                            .getString("name"));
                    mLatitude = location.getString("lat");
                    mLongitude = location.getString("long");

                    if (googleMap != null) {
                        // Parse the result and add the marker to the map

                            // Add a marker at the specified coordinates
                            LatLng position = new LatLng(Double.parseDouble(mLatitude),
                                    Double.parseDouble(mLongitude));
                            googleMap.addMarker(new MarkerOptions().position(position).title("New Marker"));

                            // Optionally, move the camera to the marker's location
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));

                    }


                }
                else if(result.contains("errors")){
                    String message = jsonObject.getString("message");
                    Toasts.toastIconError(IndividualLocationActivity.this,message);
                    //Toast.makeText(getBaseContext(), message, Toast.LENGTH_SHORT).show();
                }
                else{

                    Toasts.toastIconError(IndividualLocationActivity.this,"Error Loading Account. Please Try Again");
//                    FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

                }

            }else{

                Toasts.toastIconError(IndividualLocationActivity.this,"Loading Account Failed. Please Try Again");
//                FirebaseCrashlytics.getInstance().recordException(new Exception("Add Account Error: "+result));

            }

//            Log.d("API Success", "Data: " + jsonObject.toString());


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // You can customize the map here if needed
        // For example, enable zoom controls:
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);




        Intent intent = getIntent();
        String mName = "Test";

        // Add a marker at a specific location and move the camera
        // Example: San Francisco
//        LatLng sanFrancisco = new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
//        googleMap.addMarker(new MarkerOptions().position(sanFrancisco).title(mName));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sanFrancisco, 12)); // Zoom level 12



    }
}
