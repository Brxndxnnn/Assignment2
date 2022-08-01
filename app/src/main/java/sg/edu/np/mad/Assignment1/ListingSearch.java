package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ListingSearch extends AppCompatActivity {
    //Initialise the variables
    private String currentEmail;
    private TextView cancel;
    private TextView clearAll;
    private SearchView searchView; //SearchView Widget
    private RecyclerView searchRV; //RecyclerView for Search History
    private ArrayList<Listings> listingItems; //ArrayList storing listings in firebase
    private ArrayList<SearchHistory> searchHistoryList; //ArrayList storing searched items
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_search);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_round);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.MintCream)));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'> Recyclops </font>"));

        // Get the email of the login user:
        currentEmail = MainActivity.loggedInEmail.replace(".", "");

        listingItems = new ArrayList<>(); //Initialize listing item lists
        searchHistoryList = new ArrayList<>(); //Initialize listing item lists

        retrieveListingsFromDB(); // call method to retrieve all listing items
        RetrieveSearchHistory(); // Call Method to Retrieve Searched Items

        // Assign the Search History RecyclerView
        searchRV = findViewById(R.id.searchRecyclerView);

        // Set linear layout and adapter for Search History RecyclerView:
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        SearchHistoryAdapter shAdapter = new SearchHistoryAdapter(searchHistoryList, ListingSearch.this);
        searchRV.setLayoutManager(linearLayout);
        searchRV.setAdapter(shAdapter);

        //Assign searchView object to the SearchView widget:
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Check if item search is within the listing items ArrayList
                Boolean exist = false;
                for (Listings item : listingItems){
                    if (query.toLowerCase().equals(item.getTitle().toLowerCase())){
                        exist = true;
                        Date date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).getTime();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String strDate = dateFormat.format(date);
                        SearchHistory searchItem = new SearchHistory(query, strDate);
                        AddSearchItemToDB(searchItem.getListingName(), searchItem.getSearchTime()); // Add Search History record to firebase
                        shAdapter.notifyDataSetChanged();
                        // New Intent to go to Listing Details Page after user search for a specific listing item
                        Intent searchToDetails = new Intent(ListingSearch.this, ListingDetails.class);
                        searchToDetails.putExtra("key", item.getId());
                        searchToDetails.putExtra("Title", item.getTitle());
                        searchToDetails.putExtra("Image", item.getImageUrl());
                        searchToDetails.putExtra("Desc", item.getDesc());
                        searchToDetails.putExtra("Poster", item.getPoster());
                        searchToDetails.putExtra("Location", item.getLocation());
                        //searchToDetails.putExtra("IsLike", item.getLike());
                        startActivity(searchToDetails);
                    }

                }
                if (exist == false){
                    Toast.makeText(ListingSearch.this,"Item is not found.",Toast.LENGTH_SHORT).show();
                }
                // Clear the SearchView text after user submits the query
                searchView.setQuery("", false);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        // When user clicks Clear All, the search history of the user will be deleted
        clearAll = findViewById(R.id.clearAllText);
        clearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClearSearchHistory();
                shAdapter.notifyDataSetChanged();
            }
        });

        // When user clicks cancel, goes back to previous activity
        cancel = findViewById(R.id.cancelText);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    // Method to retrieve listing items data from FireBase
    private void retrieveListingsFromDB(){
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Listings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear list before adding data into it
                listingItems.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    Listings listings = ds.getValue(Listings.class);
                    //add model/data to list
                    listingItems.add(listings);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // Method to add the item that user search to Firebase
    private void AddSearchItemToDB(String itemName, String searchTime){
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        SearchHistory searchHistory = new SearchHistory(itemName,searchTime);
        mDatabase.child("SearchHistory").child(currentEmail).child(searchTime).setValue(searchHistory);
    }

    // Method to retrieve Search History from Firebase
    private void RetrieveSearchHistory(){
        DatabaseReference searchHistoryRef = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SearchHistory").child(currentEmail);
        searchHistoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchHistoryList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    SearchHistory searchItems = ds.getValue(SearchHistory.class);
                    //add model/data to list
                    searchHistoryList.add(searchItems);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    // Method to clear search history
    public void ClearSearchHistory(){
        DatabaseReference searchHistoryRef = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("SearchHistory").child(currentEmail);
        searchHistoryRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Cleared Search History!", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Search History not cleared....", Toast.LENGTH_LONG).show();
            }
        });
        searchHistoryList.clear();
    }
}