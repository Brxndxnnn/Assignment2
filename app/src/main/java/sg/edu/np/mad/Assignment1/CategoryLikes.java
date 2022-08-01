package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryLikes extends AppCompatActivity {

    private ArrayList<Listings> listingsArrayList = new ArrayList<>();
    private ListingAdapter listingAdapter;
    public boolean alreadyExecuted = false;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView listings;
    private FirebaseAuth mAuth;

    private String cate, userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_likes);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");

        listings = (RecyclerView) findViewById(R.id.cat_likes_recyc); // Find recycler view that store the categories
        listings.setLayoutManager(new LinearLayoutManager(CategoryLikes.this));

        gridLayoutManager = new GridLayoutManager(CategoryLikes.this, 2 , GridLayoutManager.VERTICAL, false);

        listings.setAdapter(listingAdapter);

        Intent intent = getIntent();
        cate = intent.getStringExtra("key");

        // Pull if user have liked listings
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("likesCategory").child(cate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Get data type
                String type = ((Object) task.getResult().getValue()).getClass().getSimpleName();

                if (type.equals("ArrayList")) {
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    loadListingsFromFirebase(likes); // Method to populate liked listings in its respective place
                } else {
                    // currently empty
                }
            }
        });
    }

    // Method to find and populate listings in created categories
    @Override
    public void onResume() {
        super.onResume();
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("likesCategory").child(cate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // Get data type
                String type = ((Object) task.getResult().getValue()).getClass().getSimpleName();

                if (type.equals("ArrayList")) {
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    loadListingsFromFirebase(likes); // Populate liked listings in the respective categories from Firebase

                } else {
                    ArrayList likes = new ArrayList<String>();
                    loadListingsFromFirebase(likes);
                }
            }
        });
    }

    // Method to retrieve user's liked listings
    private void loadListingsFromFirebase(ArrayList likes) {

        //Getting Realtime Database Instance
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Listings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear list before adding data into it
                listingsArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    //get data
                    final String id = ds.child("id").getValue(String.class);

                    if ((likes != null && likes.contains(id))) {
                        Listings listings = ds.getValue(Listings.class);
                        Log.d("Listings", "Got data: " + id);
                        //add model/data to list
                        listingsArrayList.add(listings); // Add liked listings into list
                    }
                }
                listings.setLayoutManager(gridLayoutManager);
                // Setup adapter
                listingAdapter = new ListingAdapter(CategoryLikes.this, listingsArrayList, true);
                // Set adapter to recyclerview
                listings.setAdapter(listingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}