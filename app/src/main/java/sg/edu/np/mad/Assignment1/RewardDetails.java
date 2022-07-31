package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RewardDetails extends AppCompatActivity
{
    //Initialising variables
    String title;
    String image;
    Integer qty;
    Integer point;
    Integer availPoints;
    DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward_details);

        //Calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.MintCream)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // Showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Assigning layout ID's
        ImageView logoImage = findViewById(R.id.logoImageDetails);
        TextView rewardTitle =  findViewById(R.id.rewardTitleDetails);
        TextView pointRequired =  findViewById(R.id.pointexchange);
        Button btn_redeem = findViewById(R.id.redeem_btn);

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
                    availPoints = Integer.valueOf(String.valueOf(task.getResult().getValue())); //Retrieve existing points
                }

                //User not found in database
                else
                {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });

        //Getting Intent values from RewardAdapter (When reward listing is pressed on)
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        image = intent.getStringExtra("Logo");
        qty = intent.getIntExtra("Qty", 0);
        point = intent.getIntExtra("Points", 0);

        //Setting the data saved in Intent
        Glide.with(this).load(image).into(logoImage);
        rewardTitle.setText(title);
        pointRequired.setText(point.toString());

        //If Redeem Now button is clicked
        btn_redeem.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (point > availPoints)
                {
                    Toast.makeText(RewardDetails.this, "Points Insufficient to Redeem Voucher!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    deductPoints();
                    Toast.makeText(RewardDetails.this, "Successfully Redeem Voucher!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    //Method to deduct points for claiming voucher
    private void deductPoints()
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
                    availPoints = Integer.valueOf(String.valueOf(task.getResult().getValue()));  //Retrieve existing points
                    availPoints = availPoints - point;    // Deduct required point from current points
                    myDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("points").setValue(availPoints).addOnSuccessListener(new OnSuccessListener<Void>()
                    {
                        @Override
                        public void onSuccess(Void unused)
                        {
                            Toast.makeText(RewardDetails.this, point.toString() + " Points Deducted!", Toast.LENGTH_SHORT).show();
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