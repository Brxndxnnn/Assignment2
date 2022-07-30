package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.util.Log;
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

public class VideoFragment extends Fragment {

    //Initialising Variables
    private Context mContext;

    public VideoFragment(){
        // require a empty public constructor
    }

    private RecyclerView eduVideos;
    private static ArrayList<ModelVideos> videosArrayList = new ArrayList<>();
    private AdapterVideo adapterVideo;
    public boolean alreadyExecuted = false;

    // Assignment 2
    private FirebaseAuth mAuth;
    private String userEmail;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        // ASG2
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // Current email is the key
        userEmail = userEmail.replace(".", "");

        //Assigning RecyclerView
        eduVideos = (RecyclerView) view.findViewById(R.id.eduVideos);

        // Retrieve data from Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        ref.child(userEmail).child("tutoLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                } else {
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    loadVideosFromFirebase(likes); // Call method
                }
            }
        });
        return view;
    }



    private void loadVideosFromFirebase(ArrayList likes){

        if(!alreadyExecuted){

            //Get Realtime Database instance
            DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Videos");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //clear list before adding data into it
                    videosArrayList.clear();

                    for (DataSnapshot ds: snapshot.getChildren()){
                        //get data
                        String id = ds.child("id").getValue(String.class);
                        String timestamp = ds.child("timestamp").getValue(String.class);
                        String title = ds.child("title").getValue(String.class);
                        String videoUrl = ds.child("videoUrl").getValue(String.class);
                        Boolean isLikes = (likes != null && likes.contains(id));
                        Log.d("testvid", String.valueOf(likes));
                        ModelVideos modelVideos = new ModelVideos(id, title, timestamp, videoUrl, isLikes);
                        //add model/data to list
                        videosArrayList.add(modelVideos);
                    }
                    //setup adapter
                    adapterVideo = new AdapterVideo(mContext, videosArrayList); //was dbHandler.getUsers()
                    //set adapter to recyclerview
                    eduVideos.setAdapter(adapterVideo);
                    adapterVideo.updateData(videosArrayList);
                    alreadyExecuted = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            //setup adapter
            adapterVideo = new AdapterVideo(mContext, videosArrayList); //was dbHandler.getUsers()
            //set adapter to recyclerview
            eduVideos.setAdapter(adapterVideo);
        }
    }
}