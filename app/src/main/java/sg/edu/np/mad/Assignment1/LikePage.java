package sg.edu.np.mad.Assignment1;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class LikePage extends AppCompatActivity {

    private TabAdapter tabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like_page);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.MintCream)));

        actionBar.setTitle("Likes");
        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout); // To switch view between liked listings and tutorials

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        tabAdapter.addFragment(new LikedListingsFragment(), "Listing"); // Directed to liked items page for listings
        tabAdapter.addFragment(new TutorialFragment(), "Tutorial"); // Directed to liked items page for tutorials

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back to previous activity
        finish();
        return super.onSupportNavigateUp();
    }
}
