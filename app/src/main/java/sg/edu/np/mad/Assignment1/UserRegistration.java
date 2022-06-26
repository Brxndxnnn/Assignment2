package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UserRegistration extends AppCompatActivity {
    EditText registerUsername, registerEmail, registerPassword, registerPassword2;
    Button registerButton;
    TextView loginWord;
    FirebaseAuth fAuth1;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //Action Bar CODES//
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.PaleGreen)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        //Action Bar CODES//

        //Registering the new user CODES//
        registerUsername = findViewById(R.id.registerUsernameText);
        registerEmail = findViewById(R.id.registerEmailText);
        registerPassword = findViewById(R.id.registerPasswordText);
        registerPassword2 = findViewById(R.id.reenterpasswordText);
        registerButton = findViewById(R.id.registerButton);
        loginWord = findViewById(R.id.loginText);
        fAuth1 = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = registerEmail.getText().toString().replace(".", "").trim();
                String username = registerUsername.getText().toString().trim();
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String password2 = registerPassword2.getText().toString().trim();


                if(TextUtils.isEmpty(username)){
                    registerUsername.setError("Username is required");
                    return;
                }

                else if(username.length() < 4){
                    registerUsername.setError("Username must be more than 3 Characters");
                    return;
                }

                else if(TextUtils.isEmpty(email)){
                    registerEmail.setError("Email is required");
                    return;
                }

                else if(TextUtils.isEmpty(password)){
                    registerPassword.setError("Password is required");
                    return;
                }

                else if(password.length() < 6){
                    registerPassword.setError("Password must be more than 5 Characters");
                    return;
                }

                else if(TextUtils.isEmpty(password)){
                    registerPassword2.setError("Please Enter your password");
                    return;
                }

                else if(!password2.equals(password)){
                    registerPassword2.setError("Password not the same as above");
                    return;
                }

                //Registering the user in FireBase
                fAuth1.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(UserRegistration.this, "User Created, you may now login",Toast.LENGTH_SHORT).show();

                            writeNewUser(id, email, username);

                            Intent intent = new Intent(getApplicationContext(), UserLogin.class);
                            intent.putExtra("registeredemail", email);

                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(UserRegistration.this, "Error!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        //Registering the new user CODES//


        //Go to LOGIN Page if account exist CODES//
        loginWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserLogin.class));
                finish();
            }
        });
        //Go to LOGIN Page if account exist CODES//
    }

    public void writeNewUser(String id, String email, String name) {
        User user = new User(email, name);

        mDatabase.child("Users").child(id).setValue(user);
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