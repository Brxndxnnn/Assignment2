package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.Viewholder> {

    Context context;
    ArrayList<Listings> listingsArrayList;
    LayoutInflater inflater;

    public ListingAdapter(Context context, ArrayList<Listings> listingsArrayList) {
        this.context = context;
        this.listingsArrayList = listingsArrayList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_grid_layout, parent, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Listings listings = listingsArrayList.get(position);

        if(listings.image == null){
            Log.d("Image", "Image nt found");
        }
        else{
            Log.d("Image", "Image found");

        }

        holder.listingImg.setImageURI(listings.image);
        holder.listingTitle.setText(listings.title);
    }

    @Override
    public int getItemCount() {
        return listingsArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView listingImg;
        TextView listingTitle;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            listingImg = itemView.findViewById(R.id.listingImg);
            listingTitle = itemView.findViewById(R.id.listingTitle);
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
