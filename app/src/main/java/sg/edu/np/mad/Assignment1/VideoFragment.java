package sg.edu.np.mad.Assignment1;

import android.content.Context;
import android.graphics.ColorSpace;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoFragment extends Fragment {

    private Context mContext;

    public VideoFragment(){
        // require a empty public constructor
    }

    private RecyclerView eduVideos;

    private static ArrayList<ModelVideos> videosArrayList = new ArrayList<>();

    private AdapterVideo adapterVideo;

    public boolean alreadyExecuted = false;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        eduVideos = (RecyclerView) view.findViewById(R.id.eduVideos);

        loadVideosFromFirebase();


        return view;
    }



    private void loadVideosFromFirebase(){

        if(!alreadyExecuted){
            Log.d("Firebase", "Requested");

            DatabaseReference ref = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Videos");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    videosArrayList.clear();
                    //clear list before adding data into it
                    for (DataSnapshot ds: snapshot.getChildren()){
                        //get data
                        ModelVideos modelVideos = ds.getValue(ModelVideos.class);
                        //add model/data to list
                        videosArrayList.add(modelVideos);
                    }
                    //setup adapter
                    adapterVideo = new AdapterVideo(mContext, videosArrayList); //was dbHandler.getUsers()
                    //set adapter to recyclerview
                    eduVideos.setAdapter(adapterVideo);

                    alreadyExecuted = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Log.d("Firebase", "No more Requested");

            //setup adapter
            adapterVideo = new AdapterVideo(mContext, videosArrayList); //was dbHandler.getUsers()
            //set adapter to recyclerview
            eduVideos.setAdapter(adapterVideo);
        }
    }
}