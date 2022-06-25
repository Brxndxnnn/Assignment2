package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    static String loggedInEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setLogo(R.mipmap.ic_launcher_round);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.MintCream)));
        actionBar.setTitle(Html.fromHtml("<font color='#000000'> Recyclops </font>"));


        bottomNavigationView = findViewById(R.id.bottomnavbar);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
            }
            else {
                loggedInEmail = extras.getString("LoggedInEmail");
            }
        }
    }

    HomeFragment homeFragment = new HomeFragment();
    VideoFragment videoFragment = new VideoFragment();
    UploadFragment uploadFragment = new UploadFragment();
    ListingFragment listingFragment = new ListingFragment();
    ProfileFragment profileFragment = new ProfileFragment();



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, homeFragment).commit();
                return true;

            case R.id.video:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, videoFragment).commit();
                return true;

            case R.id.upload:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, uploadFragment).commit();
                return true;

            case R.id.reward:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, listingFragment).commit();
                return true;

            case R.id.userprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, profileFragment).commit();
                return true;
        }
        return false;
    }

    public boolean onButtonSelected(View view) {

        switch (view.getId()) {
            case R.id.button:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, videoFragment).commit();
                bottomNavigationView.setSelectedItemId(R.id.video);
                return true;

            case R.id.button2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, uploadFragment).commit();
                bottomNavigationView.setSelectedItemId(R.id.upload);
                return true;

            case R.id.button3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, listingFragment).commit();
                bottomNavigationView.setSelectedItemId(R.id.reward);
                return true;

            case R.id.button4:
                Intent intent3 = new Intent(view.getContext(), FAQHelp.class);
                startActivity(intent3);
                return true;
        }
        return false;
    }




}