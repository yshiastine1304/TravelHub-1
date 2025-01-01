package com.personal.development.travelhub;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView selectedLocationText;
    private MaterialButton confirmButton;
    private ImageButton backButton;
    private LatLng selectedLocation;
    private String selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        selectedLocationText = findViewById(R.id.selected_location_text);
        confirmButton = findViewById(R.id.confirm_button);
        backButton = findViewById(R.id.back_button);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupConfirmButton();
        setupBackButton();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set map type to hybrid
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        // Enable my location
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // Set default location to Moalboal
        LatLng moalboal = new LatLng(9.9327, 123.4000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moalboal, 12));

        // Set up long click listener
        mMap.setOnMapLongClickListener(latLng -> {
            mMap.clear();
            selectedLocation = latLng;
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
            getAddressFromLocation(latLng);
        });
    }

    private void getAddressFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder fullAddress = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    fullAddress.append(address.getAddressLine(i));
                    if (i < address.getMaxAddressLineIndex()) {
                        fullAddress.append(", ");
                    }
                }
                selectedAddress = fullAddress.toString();
                selectedLocationText.setText(selectedAddress);
                selectedLocationText.setVisibility(View.VISIBLE);
                confirmButton.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            selectedLocationText.setText("Unable to get address. Please try again.");
            selectedLocationText.setVisibility(View.VISIBLE);
        }
    }

    private void setupConfirmButton() {
        confirmButton.setOnClickListener(v -> {
            if (selectedLocation != null && selectedAddress != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedPlace", selectedAddress);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> finish());
    }
}