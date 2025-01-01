package com.personal.development.travelhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private TextInputEditText searchInput;
    private ListView searchResults;
    private List<Attraction> allAttractions;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.search_input);
        searchResults = findViewById(R.id.search_results);

        initializeAttractions();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        searchResults.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAttractions(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchResults.setOnItemClickListener((parent, view, position, id) -> {
            Attraction selectedAttraction = allAttractions.get(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selectedPlace", selectedAttraction.getName());
            resultIntent.putExtra("latitude", selectedAttraction.getLatitude());
            resultIntent.putExtra("longitude", selectedAttraction.getLongitude());
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void initializeAttractions() {
        allAttractions = new ArrayList<>();

        allAttractions.add(new Attraction(
                "Pescador Island",
                "A small island known for its stunning coral walls and diverse marine life.",
                "9.9231035", "123.3410596",
                R.drawable.pescador_island,
                "Nature",
                "English, Cebuano",
                "All year round",
                "₱100",
                "₱50"
        ));
        allAttractions.add(new Attraction(
                "Sardine Run",
                "Experience swimming with millions of sardines in a spectacular underwater show.",
                "9.9489659", "123.3627877",
                R.drawable.sardine_run,
                "Nature",
                "English, Cebuano",
                "Year-round, best from November to May",
                "Free",
                "₱20"
        ));
        allAttractions.add(new Attraction(
                "White Beach",
                "A beautiful stretch of white sand perfect for relaxation and sunbathing.",
                "9.9767197", "123.3529045",
                R.drawable.white_beach,
                "Nature",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱30"
        ));
        allAttractions.add(new Attraction(
                "Panagsama Beach",
                "A popular spot for diving and snorkeling with a vibrant nightlife scene.",
                "9.9464207", "123.3659773",
                R.drawable.panagsama_beach,
                "Nature",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱25"
        ));
        allAttractions.add(new Attraction(
                "Lambug Beach",
                "A quiet and less crowded beach with crystal clear waters and soft sand.",
                "9.8532072", "123.3583116",
                R.drawable.lambug_beach,
                "Nature",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱40"
        ));
        allAttractions.add(new Attraction(
                "Moalboal Basdaku Beach",
                "A long stretch of white sand beach perfect for swimming and water activities.",
                "9.9842719", "123.3663112",
                R.drawable.basdaku_beach,
                "Nature",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱35"
        ));
        allAttractions.add(new Attraction(
                "Turtle Point",
                "A popular snorkeling spot where you can swim with sea turtles in their natural habitat.",
                "9.9488073", "123.3624279",
                R.drawable.turtle_point,
                "Nature",
                "English, Cebuano",
                "All year round",
                "₱50",
                "₱25"
        ));

        // Adventure
        allAttractions.add(new Attraction(
                "Kawasan Falls",
                "A series of stunning turquoise waterfalls surrounded by lush jungle. Popular for canyoneering.",
                "9.8561635", "123.3627206",
                R.drawable.kawasan_falls,
                "Adventure",
                "English, Cebuano",
                "All year round, best during dry season (March to May)",
                "₱45",
                "₱100"
        ));
        allAttractions.add(new Attraction(
                "Canyoneering Adventure",
                "An exhilarating journey through canyons, jumping off cliffs, and swimming in turquoise waters.",
                "9.8785224", "123.3564376",
                R.drawable.canyoneering,
                "Adventure",
                "English, Cebuano",
                "All year round, best during dry season (March to May)",
                "₱1500",
                "₱100"
        ));
        allAttractions.add(new Attraction(
                "Scuba Diving",
                "Explore vibrant coral reefs and encounter diverse marine life in Moalboal's world-class dive sites.",
                "9.9484375", "123.3587005",
                R.drawable.scuba_diving,
                "Adventure",
                "English, Cebuano",
                "All year round",
                "₱2500",
                "₱25"
        ));

        // Food
        allAttractions.add(new Attraction(
                "Chili Peppers Restaurant",
                "A popular spot offering a mix of Filipino and Western cuisine with a great view of the sea.",
                "9.9486274", "123.3608697",
                R.drawable.chili_peppers,
                "Food",
                "English, Cebuano",
                "All year round",
                "N/A",
                "₱25"
        ));
        allAttractions.add(new Attraction(
                "Lantaw Restaurant",
                "Enjoy local Filipino dishes with a stunning view of the ocean and nearby islands.",
                "9.9486243", "123.363353",
                R.drawable.lantaw_restaurant,
                "Food",
                "English, Cebuano",
                "All year round",
                "N/A",
                "₱25"
        ));
        allAttractions.add(new Attraction(
                "The Three Bears",
                "A cozy café offering delicious breakfast, brunch, and coffee in a relaxed atmosphere.",
                "9.9489471", "123.367464",
                R.drawable.three_bears,
                "Food",
                "English, Cebuano",
                "All year round",
                "N/A",
                "₱25"
        ));

        // Cultural and Historical
        allAttractions.add(new Attraction(
                "Moalboal Church",
                "A historic church built in the 19th century, showcasing Spanish colonial architecture.",
                "9.9377566", "123.3732363",
                R.drawable.moalboal_church,
                "Cultural",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱15"
        ));
        allAttractions.add(new Attraction(
                "Moalboal Market",
                "Experience local life and culture at this bustling market selling fresh produce and seafood.",
                "9.9367327", "123.3902955",
                R.drawable.moalboal_market,
                "Cultural",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱15"
        ));
        allAttractions.add(new Attraction(
                "Basdiot Fiesta",
                "Annual festival celebrated in May, featuring colorful parades, traditional dances, and local delicacies.",
                "9.9473664", "123.3565914",
                R.drawable.basdiot_fiesta,
                "Cultural",
                "English, Cebuano",
                "May",
                "Free",
                "₱35"
        ));

        // Relaxation
        allAttractions.add(new Attraction(
                "Moalboal Yoga",
                "Join beachfront yoga classes and meditation sessions for ultimate relaxation.",
                "9.9500757", "123.3645329",
                R.drawable.moalboal_yoga,
                "Relaxation",
                "English",
                "All year round",
                "₱500",
                "₱25"
        ));
        allAttractions.add(new Attraction(
                "Moalboal Spa and Wellness Center",
                "Indulge in traditional Filipino massages and spa treatments for a rejuvenating experience.",
                "9.9504466", "123.3612396",
                R.drawable.moalboal_spa,
                "Relaxation",
                "English, Cebuano",
                "All year round",
                "₱1000",
                "₱25"
        ));
        allAttractions.add(new Attraction(
                "Sunset Viewing at Panagsama Beach",
                "Enjoy breathtaking sunsets over the Tanon Strait, perfect for relaxation and photography.",
                "9.95026", "123.3648281",
                R.drawable.panagsama_sunset,
                "Relaxation",
                "English, Cebuano",
                "All year round",
                "Free",
                "₱25"
        ));
    }

    private void filterAttractions(String query) {
        List<String> filteredNames = new ArrayList<>();
        for (Attraction attraction : allAttractions) {
            if (attraction.getName().toLowerCase().contains(query.toLowerCase()) ||
                    attraction.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filteredNames.add(attraction.getName());
            }
        }
        adapter.clear();
        adapter.addAll(filteredNames);
        adapter.notifyDataSetChanged();
    }
}

