package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatUserProfile extends AppCompatActivity {

    //Initialising the variables
    public String name, email;
    ImageView profilePic;
    TextView username, emailaddress;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_profile);

        //Getting Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        email = intent.getStringExtra("Email");

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(Html.fromHtml("<font color='#000000'>"+ name +" Profile</font>"));

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Beige)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Initialising the ImageView and TextView
        profilePic = (ImageView) findViewById(R.id.imageView5);
        username = (TextView) findViewById(R.id.textView8);
        emailaddress = (TextView) findViewById(R.id.textView9);

        //Setting text to the Text View
        username.setText(name);
        emailaddress.setText(email);

        //Load the Profile pic of user/receiver
        loadProfilePic();
    }

    /**
     * This method is to load the profile pic of the Receiver using the Database.
     */
    private void loadProfilePic(){
        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Finding Picture in Realtime Database through current User Email Address
        mDatabase.child("Users").child(email.replace(".", "").trim()).child("profilepicUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                //Check if user has existing profile pic
                if (task.getResult().exists()) {
                    Glide.with(ChatUserProfile.this).load(task.getResult().getValue()).into(profilePic);
                }
                //Set User Profile Pic to ImageView
                else {
                    Log.e("firebase", "Error getting data", task.getException());
                }
            }
        });
    }

    /**
     * This method is to return to previous activity if back button is pressed.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}