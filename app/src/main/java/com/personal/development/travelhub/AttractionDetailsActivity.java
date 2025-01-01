package com.personal.development.travelhub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AttractionDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Attraction attraction;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction_details);

        // Get the attraction data from the intent
        attraction = (Attraction) getIntent().getSerializableExtra("attraction");

        // Initialize views
        ImageView imageView = findViewById(R.id.attraction_image);
        TextView nameTextView = findViewById(R.id.attraction_name);
        TextView categoryTextView = findViewById(R.id.attraction_category);
        TextView descriptionTextView = findViewById(R.id.attraction_description);
        TextView languageTextView = findViewById(R.id.attraction_language);
        TextView seasonsTextView = findViewById(R.id.attraction_seasons);
        TextView entranceFeeTextView = findViewById(R.id.attraction_entrance_fee);
        final TextView busFareTextView = findViewById(R.id.attraction_bus_fare);
        Button selectButton = findViewById(R.id.select_button);

        // Set attraction details
        imageView.setImageResource(attraction.getImageResourceId());
        nameTextView.setText(attraction.getName());
        categoryTextView.setText(attraction.getCategory());
        descriptionTextView.setText(attraction.getDescription());
        languageTextView.setText("Primary Language: " + attraction.getPrimaryLanguage());
        seasonsTextView.setText("Best Seasons: " + attraction.getSeasons());
        entranceFeeTextView.setText("Entrance Fee: " + attraction.getEntranceFee());
        busFareTextView.setText("Bus Fare: " + attraction.getBusFare());

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastLocation(busFareTextView);
        }

        // Set up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Set up the select button
        selectButton.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedAttraction", attraction.getName());
            resultIntent.putExtra("latitude", attraction.getLatitude());
            resultIntent.putExtra("longitude", attraction.getLongitude());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void getLastLocation(final TextView busFareTextView) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        String calculatedFare = attraction.calculateBusFare(location.getLatitude(), location.getLongitude());
                        busFareTextView.setText("Estimated Bus Fare: " + calculatedFare);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation(findViewById(R.id.attraction_bus_fare));
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                TextView busFareTextView = findViewById(R.id.attraction_bus_fare);
                busFareTextView.setText("Bus Fare: Unable to calculate (Location permission denied)");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker for the attraction and move the camera
        LatLng attractionLocation = new LatLng(
                Double.parseDouble(attraction.getLatitude()),
                Double.parseDouble(attraction.getLongitude())
        );
        mMap.addMarker(new MarkerOptions().position(attractionLocation).title(attraction.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(attractionLocation, 12));

        // Set the map type to hybrid
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        setupMapSettings();
    }

    private void setupMapSettings() {
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
        }
    }
}