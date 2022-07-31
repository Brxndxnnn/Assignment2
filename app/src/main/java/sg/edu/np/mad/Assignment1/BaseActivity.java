package sg.edu.np.mad.Assignment1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity {

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /** Done by Brandon
     * When Activity is onPause, set the User Online status to offline. Associated with the Chat feature.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        //add details to image in firebase
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Status", 0);

        mDatabase.child(MainActivity.loggedInEmail.replace(".", ""))
                .updateChildren(hashMap);
    }

    /**
     * When Activity is Resumed, set User Online. Associated with the Chat feature.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        //add details to image in firebase
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Status", 1);

        mDatabase.child(MainActivity.loggedInEmail.replace(".", ""))
                .updateChildren(hashMap);
    }
}
