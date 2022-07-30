package sg.edu.np.mad.Assignment1;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RewardList extends AppCompatActivity
{
    //Initialising variables
    RecyclerView rewardRecyclerView; //RecyclerView for Reward Listings
    RewardAdapter rewardAdapter;
    ArrayList<Rewards> rewardsArrayList = new ArrayList<>(); //ArrayList storing Reward Listings in Firebase

    Integer points;
    DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_list);

        //Calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000000'> Redemption </font>")); //Set title and color font
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Beige))); //Set background color

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // Showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Assigning layout ID's
        TextView point =  findViewById(R.id.pointsavail);

        //Getting Realtime Database instance
        myDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Finding available points in Realtime Database through current User Email Address
        myDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                //Check if user has existing profile
                if (task.isSuccessful())
                {
                    points = Integer.valueOf(String.valueOf(task.getResult().getValue()));
                    point.setText(String.valueOf(points));  //Setting current user Points to TextView
                }

                //User not found in database
                else
                {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });

        //Assigning Recyclerview
        rewardsArrayList = new ArrayList<>();
        rewardRecyclerView = findViewById(R.id.rewardListing);

        //Setting Linear Layout and Adapter for Reward Listing RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rewardRecyclerView.setLayoutManager(layoutManager);

        //Calling method to load reward listings from Database
        loadRewardListingsFromFirebase();

    }

    // Return to main menu (home)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadRewardListingsFromFirebase()
    {
        //Getting Realtime Database Instance
        DatabaseReference reference = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Rewards");
        reference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                //Clear list before adding data into it
                rewardsArrayList.clear();

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    //Get data
                    Rewards rewardListings = ds.getValue(Rewards.class);

                    //Add data to reward list
                    rewardsArrayList.add(rewardListings);
                }
                rewardAdapter = new RewardAdapter(RewardList.this , rewardsArrayList); //Setup adapter
                rewardRecyclerView.setAdapter(rewardAdapter); //Set adapter to recyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }
}

