package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RewardMain extends AppCompatActivity
{
    //Initialising variables
    Integer points;
    DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_main);

        //Calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000000'> Rewards </font>")); //Set title and color font
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Beige))); //Set background color

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // Showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Assigning layout ID's
        Button btn_redeem = findViewById(R.id.redeem_points_btn);
        Button btn_check_in = findViewById(R.id.checkin_btn);
        Button btn_spin = findViewById(R.id.spin_btn);
        Button btn_view = findViewById(R.id.view_btn);
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
                    points = Integer.valueOf(String.valueOf(task.getResult().getValue())); //Retrieve existing points
                    point.setText(String.valueOf(points)); //Setting current user Points to TextView
                }

                //User not found in database
                else
                {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });

        //If Redeem Points button is clicked
        btn_redeem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Intent to RewardList Activity
                Intent intent = new Intent(view.getContext(), RewardList.class);
                startActivity(intent);
            }
        });

        //If Check-In button is clicked
        btn_check_in.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Retrieving current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String todayString = year + "" + month + "" + day;

                SharedPreferences preferences = getSharedPreferences("points", 0);
                boolean currentDay = preferences.getBoolean(todayString, false);

                if (!currentDay)
                {
                    //Rewards has yet to be claimed
                    Toast.makeText(getApplicationContext(), "Daily Reward Claimed!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(todayString,true);
                    editor.apply();

                    //Rewards point allocating
                    addPoints();
                }

                else
                {
                    //Rewards received
                    Toast.makeText(getApplicationContext(), "Reward Claimed Already \nCome Back Tomorrow!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //If Spin button is clicked
        btn_spin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Intent to SpinWheel Activity
                Intent intent = new Intent(view.getContext(), SpinWheel.class);
                startActivity(intent);
            }
        });

        //If View button is clicked (Not Implemented Yet)
        btn_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Intent to ___ Activity
                Intent intent = new Intent(view.getContext(), RewardMain.class);
                startActivity(intent);
            }
        });
    }

    // Return to main menu (home)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Method to add points for daily check in
    private void addPoints()
    {
        //Finding available points in Realtime Database through current User Email Address
        myDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                //Check if user has existing profile
                if (task.getResult().exists())
                {
                    points = Integer.valueOf(String.valueOf(task.getResult().getValue()));  //Retrieve existing points
                    points = points + 5;    // Add 5 points to current points
                    myDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("points").setValue(points).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void unused)
                        {
                            Toast.makeText(RewardMain.this, "5 Points Updated!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //User not found in database
                else
                {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }
}
