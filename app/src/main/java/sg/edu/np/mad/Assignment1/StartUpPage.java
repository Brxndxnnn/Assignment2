package sg.edu.np.mad.Assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartUpPage extends AppCompatActivity {

    //Initialising variables
    Button mloginButton, mregisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_page);

        //Get current logged in User
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            String email = user.getEmail();
            Intent i = new Intent(StartUpPage.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("LoggedInEmail", email);
            startActivity(i);
            finish();
        } else {
            // User is signed out
            Log.d("D", "onAuthStateChanged:signed_out");
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Button to Login and Register Page CODES//
        mloginButton = findViewById(R.id.mainLogin);
        mregisterButton = findViewById(R.id.mainRegister);

        //If Login button is clicked
        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserLogin.class));
                finish();
            }
        });

        //If Register button is clicked
        mregisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
                finish();
            }
        });
        //Button to Login and Register Page CODES//
    }
}