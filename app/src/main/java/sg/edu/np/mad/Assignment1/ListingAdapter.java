package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.Viewholder> {

    //Initialising variables
    private Context context;
    private ArrayList<Listings> listingsArrayList;
    LayoutInflater inflater;
    DatabaseReference mDatabase;
    Boolean likesPage;
    private String key, userEmail;
    private FirebaseAuth mAuth;

    public ListingAdapter(Context context, ArrayList<Listings> listingsArrayList, Boolean likesPage) {
        this.context = context;
        this.listingsArrayList = listingsArrayList;
        this.inflater = LayoutInflater.from(context);
        this.likesPage = likesPage;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");

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

        // Show the add category button
        if (this.likesPage) {
            holder.addCat.setVisibility(View.VISIBLE);
        } else {
            holder.addCat.setVisibility(View.INVISIBLE);
        }

        holder.addCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(listings.getId());
            }
        });

        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child("Users").child(listings.poster.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    holder.listingPoster.setText(String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listingsArrayList.size();
    }

    public void updateData(ArrayList<ListingAdapter> listings) {
        this.listingsArrayList = listingsArrayList;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        //Initialising variables
        ImageView listingImg, addCat;
        TextView listingTitle, listingPoster;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            listingImg = itemView.findViewById(R.id.listingImg);
            listingTitle = itemView.findViewById(R.id.listingTitle);
            listingPoster = itemView.findViewById(R.id.postedBy);
            addCat = itemView.findViewById(R.id.add_to_cat);
            //If listing is being pressed on, enlarge chosen listing
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //start new intent
                    Intent intent = new Intent(v.getContext(), ListingDetails.class);
                    intent.putExtra("key", listingsArrayList.get(getAdapterPosition()).id);
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

    private void showDialog(String idList) {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("likesCategory").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final String key = dataSnapshot.getKey();

                    if (!key.equals("All")) {
                        arrayAdapter.add(key);
                    }

                }
                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                if (arrayAdapter.getCount() == 0) {
                    builderSingle.setMessage("You have no category.");
                    builderSingle.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                } else {
                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
                            builderInner.setMessage(strName);
                            builderInner.setTitle("You added the item into");
                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    addIntoCat(strName, idList);
                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();
                        }
                    });
                }
                builderSingle.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Method to add listings into category
    private void addIntoCat(String cate, String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("likesCategory").child(cate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String type = ((Object) task.getResult().getValue()).getClass().getSimpleName(); // Retrieve category from firebase
                if (type.equals("ArrayList")) {

                    // Means array exist
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    likes.add(id); // Store liked items into the category

                    ref.child(userEmail).child("likesCategory").child(cate).setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    // means array is empty
                    ArrayList likes = new ArrayList<String>();
                    likes.add(id);
                    ref.child(userEmail).child("likesCategory").child(cate).setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    // Method to remove listings into category
    private void removeFromCat(String cate, String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("likesCategory").child(cate).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String type = ((Object) task.getResult().getValue()).getClass().getSimpleName(); // Retrieve category from firebase
                if (type.equals("ArrayList")) {

                    // Means array exist
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    likes.remove(id); // Remove liked items from the category

                    ref.child(userEmail).child("likesCategory").child(cate).setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    // means array is empty
                    ArrayList likes = new ArrayList<String>();
                    likes.remove(id);
                    ref.child(userEmail).child("likesCategory").child(cate).setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            notifyDataSetChanged();
                        }
                    });
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
