package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {

    private final String userEmail = MainActivity.loggedInEmail.replace(".", "");
    private Context context;

    ArrayList<SearchHistory> searchHistoryData;
    // Create constructor for SearchHistoryAdapter
    public SearchHistoryAdapter(ArrayList<SearchHistory> searchHistoryData, Context context){
        this.searchHistoryData = searchHistoryData;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_listing_items, parent, false);
        return new SearchHistoryViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        SearchHistory listingItem = searchHistoryData.get(position);
        holder.listingName.setText(listingItem.getListingName());
        holder.searchTime.setText(listingItem.getSearchTime());
        String listName = holder.listingName.getText().toString();
        // Set OnClick Listener for remove icon (delete Search History record):
        holder.removeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference deleteRef = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("SearchHistory")
                        .child(userEmail);
                deleteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            SearchHistory searchItems = ds.getValue(SearchHistory.class);
                            // check if the listing name from db = listing name in the RecyclerView
                            if (searchItems.getListingName().equals(listName)){
                                ds.getRef().removeValue();
                                searchHistoryData.remove(searchItems);
                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }


                        }
                        notifyDataSetChanged();



                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    } // End of OnBindViewHolder Method

    @Override
    public int getItemCount() {
        return searchHistoryData.size();
    }
}
