package sg.edu.np.mad.Assignment1;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.Assignment1.ListingAdapter;
import sg.edu.np.mad.Assignment1.Listings;
import sg.edu.np.mad.Assignment1.R;


public class LikedListingsFragment extends Fragment {
    private ArrayList<LikesCatList> likesCatLists = new ArrayList<>();
    private LikesCatAdapter listingAdapter;
    private ArrayList<String> cateList;
    public boolean alreadyExecuted = false;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView listings;
    private FirebaseAuth mAuth;
    private String key, userEmail;
    private FloatingActionButton addFAB;

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

        addFAB = view.findViewById(R.id.fab_add_cat);
        // Show popup dialog for user to input
        addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        //Assigning Recyclerview
        listings = (RecyclerView) view.findViewById(R.id.cate_list);
        listings.setLayoutManager(new LinearLayoutManager(getContext()));

        listingAdapter = new LikesCatAdapter(likesCatLists, getContext());
        //listings.setAdapter(listingAdapter);

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        likesCatLists.clear();
        loadData();
    }
    @Override
    public void onPause() {
        super.onPause();
        loadData();
    }

    private void loadData() {
        likesCatLists.clear(); // clear list
        cateList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("likesCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Log.d("GG", "A");
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String key = dataSnapshot.getKey();
                        Boolean isKey = !key.equals("All");
                        LikesCatList likeCat = new LikesCatList(key, isKey);
                        likesCatLists.add(likeCat);
                        cateList.add(key);
                    }
                    listingAdapter = new LikesCatAdapter(likesCatLists, getContext());
                    listings.setAdapter(listingAdapter);
                } else {
                    Log.d("GG", "B");
                    Map<String, Object> values = new HashMap<>();
                    // set new category
                    values.put("All", "");
                    ref.child(userEmail).child("likesCategory").setValue(values).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            loadData();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showDialog() {
        EditText inputEditTextField = new EditText(getContext());
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Create new Category")
                .setView(inputEditTextField)
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String editTextInput = inputEditTextField.getText().toString();
                        Log.d("onclick", "editext value is: " + editTextInput);
                        if (cateList.contains(editTextInput.trim())) {
                            Toast.makeText(getContext(), "Category already exist", Toast.LENGTH_SHORT).show();
                        } else {
                            addCate(editTextInput);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    // Method to add user's input category
    private void addCate(String cate) {

        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        Map<String, Object> values = new HashMap<>();
        // Set new category
        values.put(cate, "");
        ref.child(userEmail).child("likesCategory").updateChildren(values).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                loadData();
            }
        });

    }
}