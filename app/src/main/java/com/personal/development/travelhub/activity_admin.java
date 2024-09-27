package com.personal.development.travelhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.core.util.Pair;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class activity_admin extends AppCompatActivity {

    private TextView btnOpenDatePicker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnOpenDatePicker = findViewById(R.id.time_admin_btn);

        btnOpenDatePicker.setOnClickListener(v -> {openDateRangePicker();});
    }

    private void openDateRangePicker(){
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        builder.setTitleText("Select a Date Range");

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

        materialDatePicker.addOnPositiveButtonClickListener(
                (MaterialPickerOnPositiveButtonClickListener<? super Pair<Long, Long>>) selection -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    Calendar calendarStart = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendarStart.setTimeInMillis(selection.first);

                    Calendar calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    calendarEnd.setTimeInMillis(selection.second);

                    String startDate = sdf.format(calendarStart.getTime());
                    String endDate = sdf.format(calendarEnd.getTime());

                    btnOpenDatePicker.setText("daily "+ startDate + " to " + endDate);
                });

    }
}