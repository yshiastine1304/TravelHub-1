package com.personal.development.travelhub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TripLocationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int SEARCH_REQUEST_CODE = 2;
    private static final int MAP_REQUEST_CODE = 3;
    private static final int POPULAR_ATTRACTIONS_REQUEST_CODE = 4;
    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private TextInputEditText homeCityInput;
    private TextInputEditText moalboalPlaceInput;
    private TextInputEditText browseAttractionsInput;
    private TextInputLayout moalboalPlaceLayout;
    private MaterialButton myLocationButton;
    private MaterialButton mapButton;
    private MaterialButton doneButton;
    private FusedLocationProviderClient fusedLocationClient;
    private String selectedLatitude;
    private String selectedLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_location);

        homeCityInput = findViewById(R.id.home_city_input);
        moalboalPlaceInput = findViewById(R.id.moalboal_place_input);
        browseAttractionsInput = findViewById(R.id.browse_attractions_input);
        moalboalPlaceLayout = findViewById(R.id.moalboal_place_layout);
        myLocationButton = findViewById(R.id.my_location_button);
        mapButton = findViewById(R.id.map_button);
        doneButton = findViewById(R.id.done_button);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setupMyLocationButton();
        setupMapButton();
        setupDoneButton();
        setupMoalboalPlaceInput();
        setupBrowseAttractionsInput();

        // Retrieve trip name and travel dates from intent (not displayed)
        String tripName = getIntent().getStringExtra("tripName");
        String travelDates = getIntent().getStringExtra("travelDates");
    }

    private void setupMoalboalPlaceInput() {
        moalboalPlaceInput.setFocusable(false);
        moalboalPlaceInput.setOnClickListener(v -> {
            Intent intent = new Intent(TripLocationActivity.this, SearchActivity.class);
            startActivityForResult(intent, SEARCH_REQUEST_CODE);
        });
    }

    private void setupBrowseAttractionsInput() {
        browseAttractionsInput.setFocusable(false);
        browseAttractionsInput.setOnClickListener(v -> {
            Intent intent = new Intent(TripLocationActivity.this, PopularAttractionActivity.class);
            startActivityForResult(intent, POPULAR_ATTRACTIONS_REQUEST_CODE);
        });
    }

    private void setupMyLocationButton() {
        myLocationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        LOCATION_PERMISSIONS,
                        LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                getCurrentLocation();
            }
        });
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        updateLocationDisplay(location);
                    } else {
                        Toast.makeText(this, "Unable to get location. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLocationDisplay(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder fullAddress = new StringBuilder();

                // Add address line if available
                String addressLine = address.getAddressLine(0);
                if (addressLine != null) {
                    fullAddress.append(addressLine);
                } else {
                    // If no address line, build from components
                    String featureName = address.getFeatureName();
                    String thoroughfare = address.getThoroughfare();
                    String subThoroughfare = address.getSubThoroughfare();
                    String locality = address.getLocality();
                    String subLocality = address.getSubLocality();
                    String adminArea = address.getAdminArea();
                    String subAdminArea = address.getSubAdminArea();
                    String postalCode = address.getPostalCode();
                    String countryName = address.getCountryName();

                    if (subThoroughfare != null) fullAddress.append(subThoroughfare).append(" ");
                    if (thoroughfare != null) fullAddress.append(thoroughfare).append(", ");
                    if (subLocality != null) fullAddress.append(subLocality).append(", ");
                    if (locality != null) fullAddress.append(locality).append(", ");
                    if (subAdminArea != null) fullAddress.append(subAdminArea).append(", ");
                    if (adminArea != null) fullAddress.append(adminArea).append(" ");
                    if (postalCode != null) fullAddress.append(postalCode).append(", ");
                    if (countryName != null) fullAddress.append(countryName);
                }

                homeCityInput.setText(fullAddress.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error getting location details", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupMapButton() {
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivityForResult(intent, MAP_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String selectedPlace = data.getStringExtra("selectedPlace");
            selectedLatitude = data.getStringExtra("latitude");
            selectedLongitude = data.getStringExtra("longitude");
            moalboalPlaceInput.setText(selectedPlace);
        } else if (requestCode == MAP_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String selectedPlace = data.getStringExtra("selectedPlace");
            selectedLatitude = data.getStringExtra("latitude");
            selectedLongitude = data.getStringExtra("longitude");
            moalboalPlaceInput.setText(selectedPlace);
        } else if (requestCode == POPULAR_ATTRACTIONS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String selectedAttraction = data.getStringExtra("selectedAttraction");
            selectedLatitude = data.getStringExtra("latitude");
            selectedLongitude = data.getStringExtra("longitude");
            moalboalPlaceInput.setText(selectedAttraction);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permissions denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupDoneButton() {
        doneButton.setOnClickListener(v -> {
            String homeCity = homeCityInput.getText().toString();
            String moalboalPlace = moalboalPlaceInput.getText().toString();

            if (!homeCity.isEmpty() && !moalboalPlace.isEmpty()) {
                // TODO: Save location data and navigate to the next screen or finish the process
                String message = "Home City: " + homeCity + "\nPlace in Moalboal: " + moalboalPlace;
                if (selectedLatitude != null && selectedLongitude != null) {
                    message += "\nLatitude: " + selectedLatitude + "\nLongitude: " + selectedLongitude;
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                finish(); // For now, just finish the activity
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

