package sg.edu.np.mad.Assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListingSearch extends AppCompatActivity {
    //Initialise the variables
    TextView cancel;
    RecyclerView searchRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_search);

        // Create dummy data for RecyclerView
        ArrayList<Listings> recentListings = new ArrayList<>();
        Listings item1 = new Listings();
        item1.title = "Unused Liverpool Cup";
        Listings item2 = new Listings();
        item2.title = "Used Gaming Mouse";
        recentListings.add(item1);
        recentListings.add(item2);

        // Assign the Search RecyclerView
        searchRV = findViewById(R.id.searchRecyclerView);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        SearchHistoryAdapter shAdapter = new SearchHistoryAdapter(recentListings);
        searchRV.setLayoutManager(linearLayout);
        searchRV.setAdapter(shAdapter);


        cancel = findViewById(R.id.cancelText);
        // When user clicks cancel, goes back to previous activity
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





    }
}