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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileFragment extends Fragment {

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

        changeUsernamebtn = (Button) view.findViewById(R.id.button5);
        changePasswordbtn = (Button) view.findViewById(R.id.button6);
        signoutButton = (Button) view.findViewById(R.id.button7);

        username = (TextView) view.findViewById(R.id.textView8);
        email = (TextView) view.findViewById(R.id.textView9);

        email.setText("Email Address: " + MainActivity.loggedInEmail);

        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        mDatabase.child("Users").child(MainActivity.loggedInEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    username.setText("Username: " + String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });

        changeUsernamebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChangeUsername.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        changePasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ChangePassword.class);
                startActivity(intent);
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), StartUpPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        return view;
    }
}