package sg.edu.np.mad.Assignment1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {

    ArrayList<SearchHistory> searchHistoryData;
    // Create constructor for SearchHistoryAdapter
    public SearchHistoryAdapter(ArrayList<SearchHistory> searchHistoryData){
        this.searchHistoryData = searchHistoryData;
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
        //holder.listingName.setText("lanjiao");
        holder.listingName.setText(listingItem.getListingName());

    }

    @Override
    public int getItemCount() {
        return searchHistoryData.size();
    }
}
