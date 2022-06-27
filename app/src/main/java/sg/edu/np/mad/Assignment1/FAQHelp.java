package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FAQHelp extends AppCompatActivity {

    //Initialising variables
    RecyclerView recyclerView;
    List<FAQ> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqhelp);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        actionBar.setHomeAsUpIndicator(R.drawable.backbutton_icon);

        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        // Display FAQ
        recyclerView = findViewById(R.id.faq_recyclerView);

        initData();
        setRecyclerView();
    }
    
    private void setRecyclerView(){
        FAQAdapter faqAdapter = new FAQAdapter(faqList);
        recyclerView.setAdapter(faqAdapter);
        //recyclerView.setHasFixedSize(true);
    }

    private void initData(){
        //Adding the FAQ rows to the List where the FAQs are retrieved from and displayed
        faqList = new ArrayList<>();

        faqList.add(new FAQ("What is this application about?", "Recyclops is a platform that allows users to list secondhand items and learn upcycling. Unlike Carousell, which allows people to sell new or old items for cash, our application only allows users to list secondhand items in order to raise awareness on the importance of buying second-hand and upcycling. Users can utilize this platform to buy supplies for their own projects and can also contribute their upcycling suggestions. Finally, through uploaded tutorials, users will be able to learn to upcycle in unique ways."));
        faqList.add(new FAQ("What is the purpose of this application?", "Our application aims to promote upcycling through reusing and second-hand purchases in order to create a more sustainable world."));
        faqList.add(new FAQ("Who is suited to use this application?", "Everyone! This application is developed to educate people of all ages on how to safeguard the environment by upcycling items and the importance of natural resource conservation."));
        faqList.add(new FAQ("How can I contribute my upcycling ideas?", "There is an upload feature where user would be able to upload their upcycling tutorials to share it with other users."));
        faqList.add(new FAQ("Login/Register feature", "User can register for an account with Recyclops and their data will be kept secured. They can Log in using the account they Registered with to save their data."));
        faqList.add(new FAQ("Tutorials", "Feature listing all the videos teaching about Upcycling and Recycling posted by other Users."));
        faqList.add(new FAQ("Upload Feature", "Users can post short videos teaching about Upcycling and Recycling for other Users of the application to see."));
        faqList.add(new FAQ("Listings", "Feature listing all the items Users are willing to give away for free for the sake of Upcycling."));
        faqList.add(new FAQ("User Profile", "Display all the User information including the Email and Username they registered with. They are able to change their Username and Password from there as well"));
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
