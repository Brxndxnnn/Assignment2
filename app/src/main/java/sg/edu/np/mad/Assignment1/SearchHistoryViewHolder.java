package sg.edu.np.mad.Assignment1;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SearchHistoryViewHolder extends RecyclerView.ViewHolder{
    TextView listingName;

    // Constructor
    public SearchHistoryViewHolder(View item)
    {
        super(item);
        listingName = item.findViewById(R.id.listingNameText);
    }

}
