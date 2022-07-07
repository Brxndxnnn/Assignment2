package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.net.URISyntaxException;

public class ListingDetails extends AppCompatActivity {
    //Initialising variables
    TextView listingTitle, listingDesc, listingPoster, listingLocation;
    ImageView listingImage;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        //Action Bar CODES//
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.MintCream)));

        actionBar.setTitle("Listing");
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Action Bar CODES//

        //Assigning layout ID's
        listingTitle = findViewById(R.id.listingTitleDetails);
        listingDesc = findViewById(R.id.listingDescDetails);
        listingPoster = findViewById(R.id.poster);
        listingLocation = findViewById(R.id.meetupLocation);
        listingImage = findViewById(R.id.listingImageDetails);

        //Getting Intent values from ListingAdapter (when Listing is pressed on)
        Intent intent = getIntent();
        String title = intent.getStringExtra("Title");
        String image = intent.getStringExtra("Image");
        String desc = intent.getStringExtra("Desc");
        String poster = intent.getStringExtra("Poster");
        String location = intent.getStringExtra("Location");

        //Setting the data saved in Intent
        Glide.with(this).load(image).into(listingImage);
        listingTitle.setText(title);
        listingDesc.setText(desc);
        listingLocation.setText("Meet up at " + location);


        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child("Users").child(poster.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    listingPoster.setText("Posted by " + String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); // go back previous activity
        finish();
        return super.onSupportNavigateUp();
    }
}