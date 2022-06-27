package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

    //Initialising variables
    private TextView username, email;

    Button changeUsernamebtn, changePasswordbtn, signoutButton;

    DatabaseReference mDatabase;

    public ProfileFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        //Get current user that is Logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Assigning layout ID's
        changeUsernamebtn = (Button) view.findViewById(R.id.button5);
        changePasswordbtn = (Button) view.findViewById(R.id.button6);
        signoutButton = (Button) view.findViewById(R.id.button7);
        username = (TextView) view.findViewById(R.id.textView8);
        email = (TextView) view.findViewById(R.id.textView9);

        //Setting current user Email Address to TextView
        email.setText(user.getEmail());

        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    username.setText("Username: " + String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });

        //If ChangeUsername button is clicked
        changeUsernamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to ChangeUsername Activity
                Intent intent = new Intent(view.getContext(), ChangeUsername.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //If ChangePassword button is clicked
        changePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to ChangePassword Activity
                Intent intent = new Intent(view.getContext(), ChangePassword.class);
                intent.putExtra("Email", user.getEmail());
                startActivity(intent);
            }
        });

        //If SignOut button is clicked
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log current user out of application to prevent auto login
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StartUpPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }
}