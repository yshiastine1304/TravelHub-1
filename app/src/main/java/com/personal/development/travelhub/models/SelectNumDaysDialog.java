package com.personal.development.travelhub.models;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.personal.development.travelhub.R;
import com.personal.development.travelhub.GeneratedItineraryActivity; // Add this import

public class SelectNumDaysDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for the dialog
        View view = inflater.inflate(R.layout.select_num_days, container, false);

        // Reference the spinner
        Spinner selectDaysSpinner = view.findViewById(R.id.select_days_spinner);

        // Populate the spinner with data from strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), // Context
                R.array.num_days_spinner_items, // Array resource
                android.R.layout.simple_spinner_item // Layout for spinner items
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        selectDaysSpinner.setAdapter(adapter);

        // Reference the Generate button
        Button generateButton = view.findViewById(R.id.generate_btn);

        // Set up the Generate button click listener
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the GeneratedActivity (redirect to generated_itinerary.xml)
                Intent intent = new Intent(getActivity(), GeneratedItineraryActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the Close button by its ID
        View closeButton = view.findViewById(R.id.close_button);

        // Set a click listener on the Close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close the dialog
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Set dialog size and style if needed
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
