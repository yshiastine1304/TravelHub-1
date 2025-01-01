package com.personal.development.travelhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class AddTripActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private TextInputEditText tripNameInput;
    private TextInputEditText dateRangeInput;
    private MaterialButton nextButton;
    private MaterialDatePicker<androidx.core.util.Pair<Long, Long>> datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        tripNameInput = findViewById(R.id.trip_name_input);
        dateRangeInput = findViewById(R.id.date_range_input);
        nextButton = findViewById(R.id.next_button);

        setupBottomNavigation();
        setupDatePicker();
        setupNextButton();
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                navigateToActivity(Dashboard.class);
                return true;
            } else if (itemId == R.id.nav_wishlist) {
                navigateToActivity(Wishlist.class);
                return true;
            } else if (itemId == R.id.nav_trip) {
                navigateToActivity(TripsActivity.class);
                return true;
            } else if (itemId == R.id.nav_account) {
                navigateToActivity(Profile.class);
                return true;
            }
            return false;
        });
    }

    private void setupDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select Trip Dates")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .setTheme(R.style.ThemeOverlay_App_DatePicker);

        datePicker = builder.build();

        dateRangeInput.setOnClickListener(v -> datePicker.show(getSupportFragmentManager(), datePicker.toString()));

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

            calendar.setTimeInMillis(selection.first);
            String startDate = format.format(calendar.getTime());

            calendar.setTimeInMillis(selection.second);
            String endDate = format.format(calendar.getTime());

            dateRangeInput.setText(String.format("%s - %s", startDate, endDate));
        });
    }

    private void setupNextButton() {
        nextButton.setOnClickListener(v -> {
            String tripName = tripNameInput.getText().toString();
            String dateRange = dateRangeInput.getText().toString();

            if (!tripName.isEmpty() && !dateRange.isEmpty()) {
                Intent intent = new Intent(this, TripLocationActivity.class);
                intent.putExtra("tripName", tripName);
                intent.putExtra("travelDates", dateRange);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}