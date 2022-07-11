package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import sg.edu.np.mad.Assignment1.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;


    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;

    public String currentUserEmail, userEmail, image;


    DatabaseReference mDatabase;
    TextView user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Action Bar CODES//
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();
        //Action Bar CODES//

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = currentUser.getEmail();

        Intent intent = getIntent();
        userEmail = intent.getStringExtra("Name");
        image = intent.getStringExtra("Image");

        user = findViewById(R.id.textName);

        loadUserDetails();
        setListeners();
        init();
        listenMessages();
    }

    private void init(){
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                image,
                currentUserEmail
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put("SenderEmail", currentUserEmail);
        message.put("ReceiverEmail", userEmail);
        message.put("Message", binding.inputMessage.getText().toString());
        message.put("Timestamp", new Date());
        database.collection("Chat").add(message);
        binding.inputMessage.setText(null);
    }

    private void listenMessages(){
        database.collection("Chat")
                .whereEqualTo("SenderEmail", currentUserEmail)
                .whereEqualTo("ReceiverEmail", userEmail)
                .addSnapshotListener(eventListener);
        database.collection("Chat")
                .whereEqualTo("SenderEmail", userEmail)
                .whereEqualTo("ReceiverEmail", currentUserEmail)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        }
        if(value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString("SenderEmail");
                    chatMessage.receiverId = documentChange.getDocument().getString("ReceiverEmail");
                    chatMessage.message = documentChange.getDocument().getString("Message");
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate("Timestamp"));
                    chatMessage.dateObject = documentChange.getDocument().getDate("Timestamp");
                    chatMessages.add(chatMessage);
                }
            }
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }
            else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
    };

    private void loadUserDetails(){
        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child("Users").child(userEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    user.setText(String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });
    }

    private void setListeners(){
        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}