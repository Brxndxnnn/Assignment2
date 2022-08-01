package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterVideo extends RecyclerView.Adapter<AdapterVideo.HolderVideo> {

    private Context context;
    private ArrayList<ModelVideos> videosArrayList;

    // ASG 2
    private FirebaseAuth mAuth;
    DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
    private String userEmail;

    public AdapterVideo(Context context, ArrayList<ModelVideos> videosArrayList) {
        this.context = context;
        this.videosArrayList = videosArrayList;
    }


    @NonNull
    @Override
    public HolderVideo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // ASG 2
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");

        View view = LayoutInflater.from(context).inflate(R.layout.listedvideos, parent, false);

        return new HolderVideo(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HolderVideo holder, int position) {

        //Assigning current ModelVideo object in the list
        ModelVideos modelVideos = videosArrayList.get(position);

        //Assigning ModelVideos values to String variables
        String timestamp = modelVideos.getTimestamp();
        String test = modelVideos.videoUrl;

        //Formatting timestamp
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String formattedDateTime = DateFormat.format("dd/MM/yyyy hh:mm", calendar).toString();

        //Like tutorial vid (ASG 2)
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeVid(modelVideos.getId(), holder);
            }
        });

        // Dislike tutorial vid (ASG 2)
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dislikeVid(modelVideos.getId(), holder);

            }
        });

        // Change like button image depending on like status
        if (modelVideos.getLike()) {
            holder.like.setVisibility(View.VISIBLE);
            holder.dislike.setVisibility(View.INVISIBLE);
        } else {
            holder.like.setVisibility(View.INVISIBLE);
            holder.dislike.setVisibility(View.VISIBLE);
        }

        //Setting data
        holder.videoTitle.setText(modelVideos.title);
        holder.videoTime.setText(formattedDateTime);

        MediaItem mediaItem = MediaItem.fromUri(test);
        holder.player.setMediaItem(mediaItem);

        holder.player.prepare();

    }

    // Update data (ASG 2)
    public void updateData(ArrayList<ModelVideos> modelVideos) {
        this.videosArrayList = modelVideos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return videosArrayList.size(); //return size of list
    }


    //VIEW HOLDER//

    public class HolderVideo extends RecyclerView.ViewHolder{

        //Initialising variables
        PlayerView videoView;
        TextView videoTitle, videoTime;
        ExoPlayer player = new ExoPlayer.Builder(context).build();
        ImageView like, dislike; // ASG 2 (Like/Dislike feature)

        public HolderVideo(@NonNull View itemView) {
            super(itemView);

            //Assigning Layout ID's
            videoView = itemView.findViewById(R.id.videoDisplayed);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoTime = itemView.findViewById(R.id.videoTime);
            like = itemView.findViewById(R.id.like_tuto); // ASG 2 imageview for like button
            dislike = itemView.findViewById(R.id.dislike_tuto); // ASG 2 imageview for dislike button
            videoView.setPlayer(player);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Method when user disliked tutorial
    private void likeVid(String id, HolderVideo holder) {

        ref.child(userEmail).child("tutoLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList likes = new ArrayList<String>();
                likes = (ArrayList) task.getResult().getValue();
                if (likes != null) {
                    likes.remove(id); // Remove liked tutorial from user's account
                    ref.child(userEmail).child("tutoLikes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If successfully removed
                            Toast.makeText(context, "Disliked tutorial", Toast.LENGTH_SHORT).show();
                            holder.like.setVisibility(View.INVISIBLE);
                            holder.dislike.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
    }

    // Method when user liked tutorial
    private void dislikeVid(String id, HolderVideo holder) {

        Log.d("test", "likes");
        ref.child(userEmail).child("tutoLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList likes = new ArrayList<String>();
                likes = (ArrayList) task.getResult().getValue();

                if (likes != null) {
                    likes.add(id); // Add liked tutorial to user's account
                    ref.child(userEmail).child("tutoLikes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            holder.like.setVisibility(View.VISIBLE);
                            holder.dislike.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, "Tutorial liked!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.d("test", userEmail);
                    ArrayList<String> newList = new ArrayList<String>();
                    newList.add(id); // add liked tutorial to list
                    ref.child(userEmail).child("tutoLikes").setValue(newList);
                    holder.like.setVisibility(View.VISIBLE);
                    holder.dislike.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Tutorial liked!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
