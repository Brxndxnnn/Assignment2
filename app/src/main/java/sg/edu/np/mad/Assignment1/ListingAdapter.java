package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.Viewholder> {

    //Initialising variables
    private Context context;
    private ArrayList<Listings> listingsArrayList;
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

        //Get respective Listing object position in list to set
        Listings listings = listingsArrayList.get(position);

        //Loading the Image into ImageView using Glide
        Glide.with(context).load(listings.imageUrl).into(holder.listingImg);

        //Setting the Listing title into TextView
        holder.listingTitle.setText(listings.title);
    }

    @Override
    public int getItemCount() {
        return listingsArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        //Initialising variables
        ImageView listingImg;
        TextView listingTitle;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            listingImg = itemView.findViewById(R.id.listingImg);
            listingTitle = itemView.findViewById(R.id.listingTitle);

            //If listing is being pressed on, enlarge chosen listing
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start new intent
                    Intent intent = new Intent(v.getContext(), ListingDetails.class);
                    intent.putExtra("Title", listingsArrayList.get(getAdapterPosition()).title);
                    intent.putExtra("Image", listingsArrayList.get(getAdapterPosition()).imageUrl);
                    intent.putExtra("Desc", listingsArrayList.get(getAdapterPosition()).desc);
                    intent.putExtra("Poster", listingsArrayList.get(getAdapterPosition()).poster);
                    intent.putExtra("Location", listingsArrayList.get(getAdapterPosition()).location);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
}
