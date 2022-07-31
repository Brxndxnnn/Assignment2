package sg.edu.np.mad.Assignment1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;

public class SpinWheel extends AppCompatActivity
{
    //Initialising variables
    ImageView wheel;
    TextView result;
    Button btn_spin;

    Integer points;
    DatabaseReference myDatabase;

    String[] sectors = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    String finalResults;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);

        //Assigning layout ID's
        wheel = findViewById(R.id.wheel);
        result = findViewById(R.id.result);
        btn_spin = findViewById(R.id.btn_spin);

        Collections.reverse(Arrays.asList(sectors));

        //Getting Realtime Database instance
        myDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //If Redeem Points button is clicked
        btn_spin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Retrieving current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String todaySpinString = year + "" + month + "" + day;

                SharedPreferences preferences = getSharedPreferences("spinPoints", 0);
                boolean currentDay = preferences.getBoolean(todaySpinString, false);

                if (!currentDay)
                {
                    //Rewards has yet to be claimed
                    Toast.makeText(getApplicationContext(), "Daily Spin Claimed!", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean(todaySpinString,true);
                    editor.apply();

                    //Spin the wheel
                    spinWheel(view);
                }

                else
                {
                    //Rewards received
                    Toast.makeText(getApplicationContext(), "Reward Claimed Already \nCome Back Tomorrow!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void spinWheel(View view)
    {
        Random random = new Random();
        int degree = random.nextInt(360);

        RotateAnimation rotateAnimation = new RotateAnimation(0, degree + 720,      // Set 720 to take two rotation
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(3000);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
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
                        }

                        //User not found in database
                        else
                        {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                    }
                });

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                CalculatePoint(degree);
                addPoints();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
        wheel.startAnimation(rotateAnimation);
    }

    public void CalculatePoint(int degree)
    {
        // Total 360 Degree  12 Segments  Each Segment 30 Degree //
        int initialPoint = 0;
        int endPoint = 30;
        int i = 0;
        String results = null;

        do
        {
            if (degree > initialPoint && degree < endPoint)
            {
                results = sectors[i];
            }

            initialPoint += 30; endPoint += 30;
            i++;
        }
        while (results == null);

        result.setText(results);

        finalResults = results;
    }

    //Method to add points for daily check in
    private void addPoints()
    {
        //Finding available points in Realtime Database through current User Email Address
        myDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                //Check if user has existing profile
                if (task.getResult().exists())
                {
                    points = Integer.valueOf(String.valueOf(task.getResult().getValue()));  //Retrieve existing points
                    points = points + Integer.valueOf(finalResults);    // Add rewarded points to current points
                    myDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("points").setValue(points).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused)
                        {
                            Toast.makeText(SpinWheel.this, finalResults + " Points Updated!", Toast.LENGTH_SHORT).show();
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
