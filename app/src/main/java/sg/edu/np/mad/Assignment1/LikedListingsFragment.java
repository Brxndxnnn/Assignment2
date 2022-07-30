package sg.edu.np.mad.Assignment1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class LikedListingsFragment extends Fragment {
    private ArrayList<Listings> listingsArrayList = new ArrayList<>();
    private ListingAdapter listingAdapter;
    public boolean alreadyExecuted = false;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView listings;
    private FirebaseAuth mAuth;
    private String key, userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked_listings, container, false);

        // Get user and initialized
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");

        // Assigning Recyclerview
        listings = (RecyclerView) view.findViewById(R.id.listings_likes);

        // Setting GridLayout
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2 , GridLayoutManager.VERTICAL, false);
        listings.setLayoutManager(gridLayoutManager);

        // Pull if user have liked listings
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("listingLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                } else {
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    loadListingsFromFirebase(likes);
                }
            }
        });
        return view;
    }

    // Retrieve liked listing(s)
    private void loadListingsFromFirebase(ArrayList likes){

        // Getting Realtime Database Instance
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Listings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear list before adding data into it
                listingsArrayList.clear();

                for (DataSnapshot ds: snapshot.getChildren()){
                    // Get data
                    final String id = ds.child("id").getValue(String.class);

                    if ((likes != null && likes.contains(id)) ) {
                        Listings listings = ds.getValue(Listings.class);

                        // Add data to list
                        listingsArrayList.add(listings);
                    }

                }
                listings.setLayoutManager(gridLayoutManager);
                // Setup adapter
                listingAdapter = new ListingAdapter(getContext(), listingsArrayList);
                // Set adapter to recyclerview
                listings.setAdapter(listingAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}