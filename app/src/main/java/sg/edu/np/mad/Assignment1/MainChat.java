package sg.edu.np.mad.Assignment1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

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
import com.google.firebase.firestore.auth.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sg.edu.np.mad.Assignment1.databinding.ActivityMainChatBinding;

public class MainChat extends AppCompatActivity {

    private ActivityMainChatBinding binding;
    private List<ChatMessage> conversations;
    private  RecentConversationAdapter conversationAdapter;
    private FirebaseFirestore database;

    public String currentUserEmail, image;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = currentUser.getEmail();

        init();
        loadUserDetails();
        listenConversations();
    }

    private void init(){
        conversations = new ArrayList<>();
        conversationAdapter = new RecentConversationAdapter(conversations);
        binding.conversationsRecyclerView.setAdapter(conversationAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void loadUserDetails(){
        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        //Finding Username in Realtime Database through current User Email Address
        mDatabase.child("Users").child(currentUserEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    binding.textName.setText(String.valueOf(task.getResult().child("username").getValue()));
                }
            }
        });
    }

    private void listenConversations(){
        database.collection("Conversation")
                .whereEqualTo("SenderEmail", currentUserEmail)
                .addSnapshotListener(eventListener);
        database.collection("Conversation")
                .whereEqualTo("ReceiverEmail", currentUserEmail)
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null){
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    String senderId = documentChange.getDocument().getString("SenderEmail");
                    String receiverId = documentChange.getDocument().getString("ReceiverEmail");
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if(currentUserEmail.equals(senderId)){
                        chatMessage.conversationName = documentChange.getDocument().getString("ReceiverName");
                        chatMessage.conversationId = documentChange.getDocument().getString("ReceiverEmail");
                    }
                    else{
                        chatMessage.conversationName = documentChange.getDocument().getString("SenderName");
                        chatMessage.conversationId = documentChange.getDocument().getString("SenderEmail");
                    }
                    chatMessage.message = documentChange.getDocument().getString("LastMessage");
                    chatMessage.dateObject = documentChange.getDocument().getDate("Timestamp");
                    conversations.add(chatMessage);
                }
                else if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    for (int i = 0; i < conversations.size(); i++){
                        String senderId = documentChange.getDocument().getString("SenderEmail");
                        String receiverId = documentChange.getDocument().getString("ReceiverEmail");
                        if(conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)){
                            conversations.get(i).message = documentChange.getDocument().getString("LastMessage");
                            conversations.get(i).dateObject = documentChange.getDocument().getDate("Timestamp");
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (obj1,obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.conversationsRecyclerView.smoothScrollToPosition(0);
            binding.conversationsRecyclerView.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };

//    @Override
//    public void onConversationClicked(User user){
//        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
//        startActivity(intent);
//    }
}