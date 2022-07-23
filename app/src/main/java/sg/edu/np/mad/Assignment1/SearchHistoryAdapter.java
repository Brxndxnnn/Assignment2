package sg.edu.np.mad.Assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {

    ArrayList<Listings> listingData;
    // Create constructor for SearchHistoryAdapter
    public SearchHistoryAdapter(ArrayList<Listings> listingData){
        this.listingData = listingData;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recent_listing_items, null, false);
        return new SearchHistoryViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        Listings listingItem = listingData.get(position);

        holder.listingName.setText(listingItem.getTitle());

    }

    @Override
    public int getItemCount() {
        return listingData.size();
    }
}
