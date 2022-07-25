package sg.edu.np.mad.Assignment1;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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

    //Initialising variables
    private RecyclerView listings;
    private Context mContext;
    private ArrayList<Listings> listingsArrayList = new ArrayList<>();
    private ListingAdapter listingAdapter;
    public boolean alreadyExecuted = false;
    private GridLayoutManager gridLayoutManager;
    private SearchView searchView;

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

        //Assigning Recyclerview
        listings = (RecyclerView) view.findViewById(R.id.listings);

        //ZW Part: Getting the SearchView
        searchView = (SearchView) view.findViewById(R.id.searchView);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(getActivity(),ListingSearch.class);
                getActivity().startActivity(searchIntent);
                searchView.setIconified(true);
            }
        });

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Listings");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        listingsArrayList.clear();
//                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//                            Listings listings;
//                            listings = dataSnapshot.getValue(Listings.class);
//                            if(listings.getTitle().equals(query)){
//                                listingsArrayList.add(listings);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });



        //Setting GridLayout
        gridLayoutManager = new GridLayoutManager(view.getContext(), 2 , GridLayoutManager.VERTICAL, false);
        listings.setLayoutManager(gridLayoutManager);

        //Calling method to load listings from Database
        loadListingsFromFirebase();

        return view;
    }

    private void loadListingsFromFirebase(){

        //Getting Realtime Database Instance
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Listings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear list before adding data into it
                listingsArrayList.clear();

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}