package sg.edu.np.mad.Assignment1;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class TutorialFragment extends Fragment {
    private RecyclerView tutVideos;

    private static ArrayList<ModelVideos> videosArrayList = new ArrayList<>();

    private AdapterVideo adapterVideo;

    private FirebaseAuth mAuth;
    private String key, userEmail;
    public boolean alreadyExecuted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");

        //Assigning Recyclerview
        tutVideos = (RecyclerView) view.findViewById(R.id.recycler_video_frag);
        tutVideos.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterVideo = new AdapterVideo(getContext(), videosArrayList);
        tutVideos.setAdapter(adapterVideo);

        DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        // Pull if user have liked tutorials
        ref.child(userEmail).child("tutoLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                } else {
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    loadVideosFromFirebase(likes);
                }
            }
        });
        return view;
    }

    // Retrieve liked tutorial(s)
    private void loadVideosFromFirebase(ArrayList likes) {

        if (!alreadyExecuted) {

            // Get Realtime Database instance
            DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Videos");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // Clear list before adding data into it
                    videosArrayList.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        final String id = ds.child("id").getValue(String.class);
                        // Get data and id contains in list likes
                        Log.d("test", id);
                        if ((likes != null && likes.contains(id))) {

                            String timestamp = ds.child("timestamp").getValue(String.class);
                            String title = ds.child("title").getValue(String.class);
                            String videoUrl = ds.child("videoUrl").getValue(String.class);
                            Boolean isLikes = true;

                            ModelVideos modelVideos = new ModelVideos(id, title, timestamp, videoUrl, isLikes);
                            //add model/data to list
                            videosArrayList.add(modelVideos);
                        }
                    }
                    // Setup adapter
                    adapterVideo = new AdapterVideo(getContext(), videosArrayList);
                    // Set adapter to recyclerview
                    tutVideos.setAdapter(adapterVideo);
                    adapterVideo.updateData(videosArrayList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            // Setup adapter
            adapterVideo = new AdapterVideo(getContext(), videosArrayList);
            // Set adapter to recyclerview
            tutVideos.setAdapter(adapterVideo);
        }
    }
}