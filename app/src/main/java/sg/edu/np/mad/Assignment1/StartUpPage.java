package sg.edu.np.mad.Assignment1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUpPage extends AppCompatActivity {

    Button mloginButton, mregisterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Button to Login and Register Page CODES//
        mloginButton = findViewById(R.id.mainLogin);
        mregisterButton = findViewById(R.id.mainRegister);

        mloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserLogin.class));
                finish();
            }
        });

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