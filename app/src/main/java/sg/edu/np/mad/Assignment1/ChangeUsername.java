package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeUsername extends AppCompatActivity {

    //Initialising variables
    EditText newUsername;
    Button submitUsername;

    DatabaseReference mDatabase;

    ProfileFragment profileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Beige)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Assigning layout ID's
        newUsername = findViewById(R.id.usernameToChange);
        submitUsername = findViewById(R.id.submitButton);


        //if user clicked submit new username button
        submitUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Assign new username by user to String username
                String username = newUsername.getText().toString().trim();

                //Error Validation - If username is empty
                if(TextUtils.isEmpty(username)){
                    newUsername.setError("New Username is required");
                    return;
                }

                //Error Validation - If username characters less than 4
                else if(username.length() < 4){
                    newUsername.setError("Username must be more than 3 Characters");
                    return;
                }

                else {
                    //Get Realtime Database instance
                    mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
                    mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (!task.isSuccessful()) {
                                Log.e("firebase", "Error getting data", task.getException());
                            }
                            else {
                                //If data found, find user and change the Username in database accordingly
                                mDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).child("username").setValue(newUsername.getText().toString().trim());
                                Toast.makeText(ChangeUsername.this, "Username changed successfully!",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(v.getContext(), MainActivity.class));
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

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