package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class ChangePassword extends AppCompatActivity {

    //Initialising Variables
    EditText emailAddress;
    Button sendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.MintCream)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Assigning layout ID's
        emailAddress = findViewById(R.id.emailToChange);
        sendEmail = findViewById(R.id.submitButton);

        //Set email User just registered with to Email in Login page for convenience
        String registeredEmail;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
            }
            else {
                registeredEmail = extras.getString("Email");
                emailAddress.setText(registeredEmail);
            }
        }

        //When Submit button is clicked
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Assigning Email Address to String variable
                String email = emailAddress.getText().toString().trim();

                //Error Validation - If user did not enter email
                if(TextUtils.isEmpty(email)){
                    emailAddress.setError("Email is required");
                    return;
                }
                else{
                    //Sending password reset email to registered email user entered
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ChangePassword.this, "Email to change password sent successfully",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else{
                                Toast.makeText(ChangePassword.this, task.getException().toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //Action Bar - Back to Previous activity
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