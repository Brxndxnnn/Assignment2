package sg.edu.np.mad.Assignment1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class LikesCatAdapter extends RecyclerView.Adapter<LikesCatAdapter.MyViewHolder> {
    private ArrayList<LikesCatList> likesCatLists;
    private final Context context;
    private String userEmail;
    private FirebaseAuth mAuth;
    public LikesCatAdapter(ArrayList<LikesCatList> likesCatLists, Context context) {
        this.likesCatLists = likesCatLists;
        this.context = context;
    }


    @NonNull
    @Override
    public LikesCatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Get user and initialized
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");


        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_listview, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LikesCatList categoryLikes = likesCatLists.get(position);

        holder.title.setText(categoryLikes.getTitle());

        if (categoryLikes.isAll) {
            holder.imgView.setVisibility(View.VISIBLE);

        } else {
            holder.imgView.setVisibility(View.INVISIBLE);
        }

        holder.item_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CategoryLikes.class);
                intent.putExtra("key", categoryLikes.getTitle());
                view.getContext().startActivity(intent);
            }
        });

        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);

                // User do not wish to delete category
                builderSingle.setMessage("Delete " + categoryLikes.getTitle() + "?");
                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                // User wishes to delete category
                builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

                        // Remove previously created category from user's account
                        ref.child(userEmail).child("likesCategory").child(categoryLikes.getTitle()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                int newPosition = holder.getAdapterPosition();
                                likesCatLists.remove(newPosition);
                                notifyItemRemoved(newPosition);
                                notifyItemRangeChanged(newPosition, likesCatLists.size());
                                updateData(likesCatLists);
                                dialogInterface.dismiss();

                                Toast.makeText(context, "Successfully deleted.", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                builderSingle.show();
            }
        });
    }

    // Method to update change in category data
    public void updateData(ArrayList<LikesCatList> likesCatLists) {
        this.likesCatLists = likesCatLists;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return likesCatLists.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView imgView;
        private LinearLayout item_cat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cate_title);
            imgView = itemView.findViewById(R.id.like_cat_cancel);
            item_cat = itemView.findViewById(R.id.cat_item);
        }
    }
}
