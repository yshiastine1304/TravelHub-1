package com.personal.development.travelhub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommuteActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraMoveListener {

    private static final String TAG = "CommuteActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private TextView travelTimeTextView;
    private TextView distanceTextView;
    private FirebaseFirestore db;
    private String destinationName;
    private LatLng destinationLatLng;
    private String destinationImageUrl;
    private FusedLocationProviderClient fusedLocationClient;
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private LocationCallback locationCallback;
    private Polyline currentPolyline;
    private View markerInfoContainer;
    private View markerInfoPointer;
    private boolean isInfoWindowShown = false;
    private Marker destinationMarker;
    private TextView destinationNameTextView;
    private RecyclerView directionsRecyclerView;
    private DirectionsAdapter directionsAdapter;
    private List<Step> directionSteps = new ArrayList<>();
    private int currentStepIndex = 0;
    private TextView stepInstructionView;
    private TextView stepStreetNameView;
    private TextView stepDistanceView;
    private ImageView stepIconView;
    private View stepContainer;

    private static final float ZOOM_LEVEL = 18f;
    private static final float MAP_TILT = 45f;
    private static final int ANIMATION_DURATION = 3000; // 3 seconds
    private boolean initialCameraAnimationDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commute);

        markerInfoContainer = findViewById(R.id.marker_info_container);
        markerInfoPointer = findViewById(R.id.marker_info_pointer);
        travelTimeTextView = findViewById(R.id.travel_time_text_view);
        distanceTextView = findViewById(R.id.distance_text_view);
        destinationNameTextView = findViewById(R.id.destination_name_text_view);
        db = FirebaseFirestore.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        destinationName = getIntent().getStringExtra("destination");
        Log.d(TAG, "Destination name: " + destinationName);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        directionsRecyclerView = findViewById(R.id.directions_recycler_view);
        directionsAdapter = new DirectionsAdapter(directionSteps);
        directionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        directionsRecyclerView.setAdapter(directionsAdapter);

        stepInstructionView = findViewById(R.id.step_instruction);
        stepStreetNameView = findViewById(R.id.step_street_name);
        stepDistanceView = findViewById(R.id.step_distance);
        stepIconView = findViewById(R.id.step_icon);
        stepContainer = findViewById(R.id.step_container);

        stepContainer.setOnClickListener(v -> moveToNextStep());

        fetchDestinationLocation();
        setupBottomSheet();
    }

    private void setupBottomSheet() {
        View bottomSheet = findViewById(R.id.bottom_sheet);
        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void fetchDestinationLocation() {
        db.collection("attractions")
                .whereEqualTo("destination_name", destinationName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String latitude = document.getString("latitude");
                            String longitude = document.getString("longitude");
                            destinationImageUrl = document.getString("image_link_1");
                            Log.d(TAG, "Fetched lat: " + latitude + ", lng: " + longitude + ", image: " + destinationImageUrl);
                            if (latitude != null && longitude != null) {
                                double lat = Double.parseDouble(latitude);
                                double lng = Double.parseDouble(longitude);
                                destinationLatLng = new LatLng(lat, lng);
                                updateMap();
                                destinationNameTextView.setText(destinationName);
                                return;
                            }
                        }
                    }
                    Log.e(TAG, "Destination not found in Firestore");
                    Toast.makeText(this, "Destination not found", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching destination data", e);
                    Toast.makeText(this, "Error fetching destination data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnMapClickListener(latLng -> hideMarkerInfo());

        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        updateMap();
    }

    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    if (destinationLatLng != null) {
                        getRouteFromOSRM(currentLatLng, destinationLatLng);
                        updateCurrentStep(location);
                        if (initialCameraAnimationDone) {
                            float bearing = location.hasBearing() ? location.getBearing() : 0;
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(currentLatLng)
                                    .zoom(ZOOM_LEVEL)
                                    .tilt(MAP_TILT)
                                    .bearing(bearing)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
                        }
                        updateUserLocationMarker(location);
                    } else {
                        Log.e(TAG, "Destination LatLng is null");
                        Toast.makeText(CommuteActivity.this, "Destination not set. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void updateUserLocationMarker(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        float bearing = location.hasBearing() ? location.getBearing() : 0;

        BitmapDescriptor icon = createUserLocationIcon();
        if (icon == null) {
            Log.e(TAG, "Failed to create BitmapDescriptor for user location marker");
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(icon)
                .anchor(0.5f, 0.5f)
                .rotation(bearing);

        mMap.clear();
        mMap.addMarker(markerOptions);

        if (destinationMarker != null) {
            BitmapDescriptor destinationIcon = getBitmapDescriptor(R.drawable.baseline_location_pin_24);
            if (destinationIcon != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(destinationMarker.getPosition())
                        .title(destinationMarker.getTitle())
                        .icon(destinationIcon));
            }
        }

        if (currentPolyline != null) {
            mMap.addPolyline(new PolylineOptions().addAll(currentPolyline.getPoints()).color(Color.BLUE).width(10));
        }
    }

    private void getRouteFromOSRM(LatLng start, LatLng end) {
        String url = String.format(
                "https://router.project-osrm.org/route/v1/driving/%f,%f;%f,%f?overview=full&geometries=polyline&steps=true",
                start.longitude, start.latitude, end.longitude, end.latitude
        );

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching route", e);
                runOnUiThread(() -> Toast.makeText(CommuteActivity.this, "Error fetching route. Please try again.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String jsonData = response.body().string();
                        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);
                        JsonArray routes = jsonObject.getAsJsonArray("routes");
                        if (routes != null && routes.size() > 0) {
                            JsonObject route = routes.get(0).getAsJsonObject();
                            double durationSeconds = route.has("duration") ? route.get("duration").getAsDouble() : 0;
                            double distanceMeters = route.has("distance") ? route.get("distance").getAsDouble() : 0;
                            String geometry = route.has("geometry") ? route.get("geometry").getAsString() : "";
                            JsonArray legs = route.getAsJsonArray("legs");

                            List<LatLng> decodedPath = decodePolyline(geometry);
                            List<Step> steps = parseSteps(legs);

                            // Apply traffic factor to duration
                            double adjustedDuration = applyTrafficFactor(durationSeconds, distanceMeters);

                            runOnUiThread(() -> {
                                updateRouteOnMap(decodedPath);
                                updateTravelInfo(adjustedDuration, distanceMeters);
                                updateDirections(steps);
                            });
                        } else {
                            Log.e(TAG, "No route found in the response");
                            runOnUiThread(() -> Toast.makeText(CommuteActivity.this, "Unable to find a route. Please try again.", Toast.LENGTH_SHORT).show());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing JSON response", e);
                        runOnUiThread(() -> Toast.makeText(CommuteActivity.this, "Error processing route data. Please try again.", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    Log.e(TAG, "Unsuccessful response: " + response.code());
                    runOnUiThread(() -> Toast.makeText(CommuteActivity.this, "Error fetching route. Please try again.", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private double applyTrafficFactor(double durationSeconds, double distanceMeters) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        boolean isPeakHour = (currentHour >= 7 && currentHour <= 9) || (currentHour >= 16 && currentHour <= 19);

        double averageSpeed = (distanceMeters / 1000) / (durationSeconds / 3600);

        double trafficFactor;
        if (isPeakHour) {
            if (averageSpeed > 60) {
                trafficFactor = 1.8;
            } else {
                trafficFactor = 2.0;
            }
        } else {
            if (averageSpeed > 60) {
                trafficFactor = 1.5;
            } else {
                trafficFactor = 1.7;
            }
        }

        return (durationSeconds * trafficFactor) + 3600;
    }

    private void updateRouteOnMap(List<LatLng> path) {
        if (path.isEmpty()) {
            Log.e(TAG, "Empty path received");
            Toast.makeText(this, "Unable to draw route. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        runOnUiThread(() -> {
            if (currentPolyline != null) {
                currentPolyline.remove();
            }

            PolylineOptions options = new PolylineOptions()
                    .addAll(path)
                    .color(Color.BLUE)
                    .width(10);
            currentPolyline = mMap.addPolyline(options);
        });
    }

    private void updateTravelInfo(double durationSeconds, double distanceMeters) {
        int durationMinutes = (int) Math.ceil(durationSeconds / 60);
        int hours = durationMinutes / 60;
        int minutes = durationMinutes % 60;
        double distanceKm = distanceMeters / 1000;

        String durationText;
        if (hours > 0) {
            durationText = String.format("%d hr %d min", hours, minutes);
        } else {
            durationText = String.format("%d min", minutes);
        }

        runOnUiThread(() -> {
            travelTimeTextView.setText(durationText);
            distanceTextView.setText(String.format("%.1f km", distanceKm));
        });
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }
        return poly;
    }

    private void updateMap() {
        if (mMap != null && destinationLatLng != null) {
            mMap.clear();
            BitmapDescriptor icon = getBitmapDescriptor(R.drawable.baseline_location_pin_24);
            if (icon != null) {
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(destinationLatLng)
                        .title(destinationName)
                        .icon(icon);
                destinationMarker = mMap.addMarker(markerOptions);
            } else {
                Log.e(TAG, "Failed to create BitmapDescriptor for destination marker");
            }
            startLocationUpdates();
            if (!initialCameraAnimationDone) {
                startInitialCameraAnimation();
            }
        }
    }

    private void startInitialCameraAnimation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                animateCameraSequence(userLocation);
            }
        });
    }

    private void animateCameraSequence(LatLng userLocation) {
        // Step 1: Focus on user's location
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM_LEVEL), ANIMATION_DURATION, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                // Step 2: Move to destination
                mMap.animateCamera(CameraUpdateFactory.newLatLng(destinationLatLng), ANIMATION_DURATION, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        // Step 3: Zoom out to show both locations
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        builder.include(userLocation);
                        builder.include(destinationLatLng);
                        LatLngBounds bounds = builder.build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), ANIMATION_DURATION, new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                // Step 4: Return focus to user's location
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM_LEVEL), ANIMATION_DURATION, new GoogleMap.CancelableCallback() {
                                    @Override
                                    public void onFinish() {
                                        initialCameraAnimationDone = true;
                                    }

                                    @Override
                                    public void onCancel() {
                                        initialCameraAnimationDone = true;
                                    }
                                });
                            }

                            @Override
                            public void onCancel() {
                                initialCameraAnimationDone = true;
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        initialCameraAnimationDone = true;
                    }
                });
            }

            @Override
            public void onCancel() {
                initialCameraAnimationDone = true;
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(destinationMarker)) {
            if (isInfoWindowShown) {
                hideMarkerInfo();
            } else {
                showMarkerInfo();
            }
            return true;
        }
        return false;
    }

    private void showMarkerInfo() {
        if (markerInfoContainer == null) {
            markerInfoContainer = findViewById(R.id.marker_info_container);
        }

        ImageView imageView = markerInfoContainer.findViewById(R.id.destination_image_view);
        TextView nameView = markerInfoContainer.findViewById(R.id.destination_name_view);

        if (destinationImageUrl != null && !destinationImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(destinationImageUrl)
                    .into(imageView);
        }
        nameView.setText(destinationName);

        markerInfoContainer.setVisibility(View.VISIBLE);
        isInfoWindowShown = true;

        updateMarkerInfoPosition();
    }

    private void hideMarkerInfo() {
        if (markerInfoContainer != null) {
            markerInfoContainer.setVisibility(View.GONE);
        }
        isInfoWindowShown = false;
    }

    private void updateMarkerInfoPosition() {
        if (mMap == null || destinationMarker == null || markerInfoContainer == null) {
            return;
        }

        LatLng markerPosition = destinationMarker.getPosition();
        Point markerPoint = mMap.getProjection().toScreenLocation(markerPosition);
        Point infoWindowPoint = new Point(markerPoint.x, markerPoint.y - markerInfoContainer.getHeight() - 20);

        markerInfoContainer.setX(markerPoint.x - markerInfoContainer.getWidth() / 2f);
        markerInfoContainer.setY(infoWindowPoint.y);

        markerInfoPointer.setX(markerPoint.x - markerInfoPointer.getWidth() / 2f);
        markerInfoPointer.setY(markerPoint.y - markerInfoPointer.getHeight());
    }

    @Override
    public void onCameraMove() {
        if (isInfoWindowShown) {
            updateMarkerInfoPosition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
                updateMap();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMap != null && destinationLatLng != null) {
            startLocationUpdates();
        }
    }

    private BitmapDescriptor getBitmapDescriptor(int id) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, id);
        if (vectorDrawable == null) {
            Log.e(TAG, "Resource not found");
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private List<Step> parseSteps(JsonArray legs) {
        List<Step> steps = new ArrayList<>();
        if (legs == null) {
            return steps;
        }
        for (int i = 0; i < legs.size(); i++) {
            JsonObject leg = legs.get(i).getAsJsonObject();
            JsonArray stepsArray = leg.getAsJsonArray("steps");
            if (stepsArray == null) {
                continue;
            }
            for (int j = 0; j < stepsArray.size(); j++) {
                JsonObject stepObject = stepsArray.get(j).getAsJsonObject();
                String instruction = "";
                String name = "";
                double distance = 0;
                int icon = R.drawable.baseline_straight_24;
                LatLng endLocation = null;

                if (stepObject.has("maneuver")) {
                    JsonObject maneuver = stepObject.getAsJsonObject("maneuver");
                    if (maneuver != null && maneuver.has("type")) {
                        String type = maneuver.get("type").getAsString();
                        if (type.equals("turn")) {
                            String modifier = maneuver.get("modifier").getAsString();
                            if (modifier.contains("right")) {
                                icon = R.drawable.baseline_turn_right_24;
                            } else if (modifier.contains("left")) {
                                icon = R.drawable.baseline_turn_left_24;
                            }
                        }
                    }
                }

                if (stepObject.has("name")) {
                    JsonElement nameElement = stepObject.get("name");
                    if (nameElement != null && !nameElement.isJsonNull()) {
                        name = nameElement.getAsString();
                    }
                }

                if (stepObject.has("distance")) {
                    JsonElement distanceElement = stepObject.get("distance");
                    if (distanceElement != null && !distanceElement.isJsonNull()) {
                        distance = distanceElement.getAsDouble();
                    }
                }

                if (stepObject.has("maneuver")) {
                    JsonObject maneuver = stepObject.getAsJsonObject("maneuver");
                    if (maneuver != null && maneuver.has("location")) {
                        JsonArray location = maneuver.getAsJsonArray("location");
                        double lng = location.get(0).getAsDouble();
                        double lat = location.get(1).getAsDouble();
                        endLocation = new LatLng(lat, lng);
                    }
                }

                if (stepObject.has("maneuver")) {
                    JsonObject maneuver = stepObject.getAsJsonObject("maneuver");
                    if (maneuver != null && maneuver.has("instruction")) {
                        instruction = maneuver.get("instruction").getAsString();
                    }
                }

                steps.add(new Step(instruction, name, distance, icon, endLocation));
            }
        }
        return steps;
    }

    private void updateDirections(List<Step> steps) {
        directionSteps.clear();
        directionSteps.addAll(steps);
        currentStepIndex = 0;
        displayCurrentStep();
    }

    private void displayCurrentStep() {
        if (directionSteps.isEmpty() || currentStepIndex >= directionSteps.size()) {
            stepContainer.setVisibility(View.GONE);
            return;
        }

        Step currentStep = directionSteps.get(currentStepIndex);
        stepInstructionView.setText(currentStep.instruction);
        stepStreetNameView.setText(currentStep.streetName);
        stepDistanceView.setText(String.format("%.1f km", currentStep.distance / 1000));
        stepIconView.setImageResource(currentStep.icon);
        stepContainer.setVisibility(View.VISIBLE);
    }

    private void moveToNextStep() {
        if (currentStepIndex < directionSteps.size() - 1) {
            currentStepIndex++;
            displayCurrentStep();
        } else {
            Toast.makeText(this, "You have reached your destination", Toast.LENGTH_SHORT).show();
            stepContainer.setVisibility(View.GONE);
        }
    }

    private void updateCurrentStep(Location userLocation) {
        if (directionSteps.isEmpty() || currentStepIndex >= directionSteps.size()) {
            return;
        }

        Step currentStep = directionSteps.get(currentStepIndex);
        LatLng stepEndLocation = currentStep.endLocation;

        if (stepEndLocation != null) {
            float[] results = new float[1];
            Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                    stepEndLocation.latitude, stepEndLocation.longitude, results);

            if (results[0] < 20) { // If user is within 20 meters of the step end location
                moveToNextStep();
            }
        }
    }

    private static class Step {
        String instruction;
        String streetName;
        double distance;
        int icon;
        LatLng endLocation;

        Step(String instruction, String streetName, double distance, int icon, LatLng endLocation) {
            this.instruction = instruction;
            this.streetName = streetName;
            this.distance = distance;
            this.icon = icon;
            this.endLocation = endLocation;
        }
    }

    private class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.ViewHolder> {
        private List<Step> steps;

        DirectionsAdapter(List<Step> steps) {
            this.steps = steps;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.direction_step_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Step step = steps.get(position);
            holder.instructionView.setText(step.instruction);
            holder.streetNameView.setText(step.streetName);
            holder.distanceView.setText(String.format("%.1f km", step.distance / 1000));
            holder.iconView.setImageResource(step.icon);
        }

        @Override
        public int getItemCount() {
            return steps.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView iconView;
            TextView instructionView;
            TextView streetNameView;
            TextView distanceView;

            ViewHolder(View itemView) {
                super(itemView);
                iconView = itemView.findViewById(R.id.direction_icon);
                instructionView = itemView.findViewById(R.id.direction_instruction);
                streetNameView = itemView.findViewById(R.id.direction_street_name);
                distanceView = itemView.findViewById(R.id.direction_distance);
            }
        }
    }

    private BitmapDescriptor createUserLocationIcon() {
        Drawable background = ContextCompat.getDrawable(this, R.drawable.circle_background);
        Drawable foreground = ContextCompat.getDrawable(this, R.drawable.baseline_navigation_24);

        if (background == null || foreground == null) {
            Log.e(TAG, "Failed to load drawables for user location icon");
            return BitmapDescriptorFactory.defaultMarker();
        }

        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(),
                background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        background.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        background.draw(canvas);

        int left = (canvas.getWidth() - foreground.getIntrinsicWidth()) / 2;
        int top = (canvas.getHeight() - foreground.getIntrinsicHeight()) / 2;
        foreground.setBounds(left, top, left + foreground.getIntrinsicWidth(), top + foreground.getIntrinsicHeight());
        foreground.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

