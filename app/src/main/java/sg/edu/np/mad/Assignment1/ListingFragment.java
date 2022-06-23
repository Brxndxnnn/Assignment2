package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListingFragment extends Fragment {

    RecyclerView listings;
    Context mContext;
    public static ArrayList<Listings> listingsArrayList = new ArrayList<>();
    ListingAdapter listingAdapter;
    public boolean alreadyExecuted = false;
    GridLayoutManager gridLayoutManager;

    public ListingFragment(){
        // require a empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listing, container, false);

        listings = (RecyclerView) view.findViewById(R.id.listings);

        gridLayoutManager = new GridLayoutManager(view.getContext(), 2 , GridLayoutManager.VERTICAL, false);
        listings.setLayoutManager(gridLayoutManager);

        loadListingsFromFirebase();

        return view;
    }

    private void loadListingsFromFirebase(){

        if(!alreadyExecuted){
            Log.d("Firebase", "Requested");

            DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Listings");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //clear list before adding data into it
                    for (DataSnapshot ds: snapshot.getChildren()){
                        //get data
                        Listings listings = ds.getValue(Listings.class);
                        //add model/data to list
                        listingsArrayList.add(listings);
                    }

                    listings.setLayoutManager(gridLayoutManager);
                    //setup adapter
                    listingAdapter = new ListingAdapter(mContext, listingsArrayList);
                    //set adapter to recyclerview
                    listings.setAdapter(listingAdapter);


                    alreadyExecuted = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Log.d("Firebase", "No more Requested");

            //setup adapter
            listingAdapter = new ListingAdapter(mContext, listingsArrayList);
            //set adapter to recyclerview
            listings.setAdapter(listingAdapter);
        }
    }
}