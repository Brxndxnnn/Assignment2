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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserLogin extends AppCompatActivity {

    //Initialising variables
    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView registerWord, forgotPassword;
    FirebaseAuth fAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PaleGreen)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Assigning layout ID's
        loginEmail = findViewById(R.id.emailToChange);
        loginPassword = findViewById(R.id.loginPasswordText);
        loginButton = findViewById(R.id.submitButton);
        fAuth = FirebaseAuth.getInstance();
        registerWord = findViewById(R.id.signupText);
        forgotPassword = findViewById(R.id.forgotPassword);

        //Set email User just registered with to Email in Login page for convenience
        String registeredEmail;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
            }
            else {
                registeredEmail = extras.getString("registeredemail");
                loginEmail.setText(registeredEmail);
            }
        }

        //If forgot password word was clicked
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            //Intent to ChangePassword activity
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChangePassword.class));
            }
        });

        //If Register word was clicked
        registerWord.setOnClickListener(new View.OnClickListener() {
            //Intent to UserRegistration activity
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserRegistration.class));
                finish();
            }
        });

        //If login button is clicked
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Getting User inputted values and assigning to variables email & password
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                //Error Validation - If Email Address is empty
                if(TextUtils.isEmpty(email)){
                    loginEmail.setError("Email is required");
                    return;
                }

                //Error Validation - If Password is empty
                else if(TextUtils.isEmpty(password)){
                    loginPassword.setError("Password is required");
                    return;
                }

                // authenticate the user
                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserLogin.this, "Logged in successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("LoggedInEmail", email);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(UserLogin.this, "Credentials are wrong, please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), StartUpPage.class);
                startActivity(intent);
                finish();
                //this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}