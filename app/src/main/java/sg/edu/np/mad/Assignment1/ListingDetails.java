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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class ListingDetails extends AppCompatActivity {
    //Initialising variables
    TextView listingTitle, listingDesc, listingPoster, listingLocation;
    Button chatButton;
    ImageView listingImage;
    DatabaseReference mDatabase;

    // ASG 2
    private FirebaseAuth mAuth;
    private String key, userEmail;
    private Menu menu;
    Boolean isLike;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        // Get user and initialized
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        userEmail = currentUser.getEmail(); // current email is the key
        userEmail = userEmail.replace(".", "");

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
        chatButton = findViewById(R.id.chatButton);

        //Getting Intent values from ListingAdapter (when Listing is pressed on)
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        String title = intent.getStringExtra("Title");
        String image = intent.getStringExtra("Image");
        String desc = intent.getStringExtra("Desc");
        String poster = intent.getStringExtra("Poster");
        String location = intent.getStringExtra("Location");

        // Receiving Intent Values from ListingSearch Activity (when user searches a specific item):
        // Intent fromListingSearch = getIntent();
        // String listingName = fromListingSearch.getStringExtra("Title");
        // String listingPicture = fromListingSearch.getStringExtra("Image");


        //Setting the data saved in Intent
        Glide.with(this).load(image).into(listingImage);
        listingTitle.setText(title);
        listingDesc.setText(desc);
        listingLocation.setText("Meet up at " + location);


        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child(poster.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.loggedInEmail.equals(poster)){
                    Toast.makeText(ListingDetails.this, "You can't chat with yourself :)", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent1 = new Intent(ListingDetails.this, ChatActivity.class);
                    intent1.putExtra("Name", poster);
                    intent1.putExtra("Image", image);
                    startActivity(intent1);
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

    // ASG 2 (like/dislike feature)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.like_menu, menu);
        MenuItem fav = menu.findItem(R.id.item_like);
        MenuItem unfav = menu.findItem(R.id.item_dislike);
        // onView page run this.
        // Check whether user liked this listing.
        ref.child(userEmail).child("listingLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {

                } else {
                    ArrayList likes = new ArrayList<String>();
                    likes = (ArrayList) task.getResult().getValue();
                    // Return list from Firebase
                    if ((likes !=null && likes.contains(key))) {
                        // If likes contain the string, return true
                        fav.setVisible(true);
                        unfav.setVisible(false);
                    } else {
                        fav.setVisible(false);
                        unfav.setVisible(true);
                    }
                }
            }
        });
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_like:

                removeItem(key);
                return true;

            case R.id.item_dislike:

                addItem(key);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Dislike listing item method
    private void removeItem(String id) {
        MenuItem like = menu.findItem(R.id.item_like);
        MenuItem dislike = menu.findItem(R.id.item_dislike);

        Log.d("test", userEmail);
        ref.child(userEmail).child("listingLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList likes = new ArrayList<String>();
                likes = (ArrayList) task.getResult().getValue();
                if (likes != null) {
                    likes.remove(id); // Remove disliked item
                    ref.child(userEmail).child("listingLikes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            // if successfully removed .
                            like.setVisible(false);
                            dislike.setVisible(true);
                            Toast.makeText(ListingDetails.this, "Disliked item", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // Like listing item method
    private void addItem(String id) {
        MenuItem like = menu.findItem(R.id.item_like);
        MenuItem dislike = menu.findItem(R.id.item_dislike);

        ref.child(userEmail).child("listingLikes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList likes = new ArrayList<String>();
                likes = (ArrayList) task.getResult().getValue();

                if (likes != null) {
                    // if empty
                    likes.add(id); // Add liked item
                    ref.child(userEmail).child("listingLikes").setValue(likes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dislike.setVisible(false);
                            like.setVisible(true);
                            Toast.makeText(ListingDetails.this, "Item liked!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    // Add in current list and set
                    Log.d("Listing", userEmail);
                    ArrayList<String> newList = new ArrayList<String>();
                    newList.add(id);
                    ref.child(userEmail).child("listingLikes").setValue(newList);
                    dislike.setVisible(false);
                    like.setVisible(true);
                    Toast.makeText(ListingDetails.this, "Item liked!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
