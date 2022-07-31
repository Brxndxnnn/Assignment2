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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

public class ChatActivity extends BaseActivity {

    //Initialising variables
    private ActivityChatBinding binding;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private FirebaseFirestore database;
    public String conversationId = null;
    public String currentUserEmail, image;
    public static String ReceiverUsername, SenderUsername, userEmail;
    DatabaseReference mDatabase;
    TextView user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseFirestore.getInstance();

        //Action Bar CODES//
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();

        actionBar.hide();
        //Action Bar CODES//

        //Get email of current user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserEmail = currentUser.getEmail();

        //Get email and image of receiver through intent
        Intent intent = getIntent();
        userEmail = intent.getStringExtra("Name");
        image = intent.getStringExtra("Image");

        //Assigning text view
        user = findViewById(R.id.textName);

        //Calling the methods.
        init();
        loadUserDetails();
        setListeners();
        listenMessages();
    }

    /**
     * This method is to initialise and assign the respective Chat Messages list and Adapter. Set the Adapter to the Recyclerview.
     */
    private void init(){
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                image,
                currentUserEmail
        );
        binding.chatRecyclerView.setAdapter(chatAdapter);
    }

    /**
     * This method is to allow User to send the message to the respective person
     */
    private void sendMessage(){
        //Inserting the Values into the HashMap.
        HashMap<String, Object> message = new HashMap<>();
        message.put("SenderEmail", currentUserEmail);
        message.put("ReceiverEmail", userEmail);
        message.put("Message", binding.inputMessage.getText().toString());
        message.put("Timestamp", new Date());

        //Insert the Hashmap into the Firestore Database.
        database.collection("Chat").add(message);

        //Check if Conversations already exists.
        if(conversationId != null){
            Log.d("SendMessage", binding.inputMessage.getText().toString());
            updateConversation(binding.inputMessage.getText().toString());
        }

        //If doesn't exist, create new Conversation.
        else{
            Log.d("SendMessage", "gg");
            HashMap<String, Object> conversation = new HashMap<>();
            conversation.put("SenderEmail", currentUserEmail);
            conversation.put("SenderName", SenderUsername);
            conversation.put("ReceiverEmail", userEmail);
            conversation.put("ReceiverName", ReceiverUsername);
            conversation.put("LastMessage", binding.inputMessage.getText().toString());
            conversation.put("Timestamp", new Date());
            addConversation(conversation);
        }

        //Clear the Message box.
        binding.inputMessage.setText(null);
    }


    /**
     * This method is to check whether if the Message Receiver is online or offline and display the status.
     */
    private void listenReceiverAvailability(){
        //Initialising the Firebase Database
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");

        mDatabase.child(userEmail.replace(".", "")).child("Status").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //If user is online, display online status
                if (String.valueOf(snapshot.getValue()).equals("1")){
                    binding.textAvailability.setVisibility(View.VISIBLE);
                }
                //Else, hide the online status
                else{
                    binding.textAvailability.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * This method is to listen to the messages.
     */
    private void listenMessages(){
        //Specifying the Database to listen to
        database.collection("Chat")
                .whereEqualTo("SenderEmail", currentUserEmail)
                .whereEqualTo("ReceiverEmail", userEmail)
                .addSnapshotListener(eventListener);

        //Specifying the Database to listen to
        database.collection("Chat")
                .whereEqualTo("SenderEmail", userEmail)
                .whereEqualTo("ReceiverEmail", currentUserEmail)
                .addSnapshotListener(eventListener);
    }

    /**
     * This method is to check and listen to the messages and send it to the Recyclerview.
     */
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        //If no error.
        if (error != null){
            return;
        }

        //If value is not null.
        if(value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED){

                    //Set the Chat message and add it to the Chat Messages list.
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString("SenderEmail");
                    chatMessage.receiverId = documentChange.getDocument().getString("ReceiverEmail");
                    chatMessage.message = documentChange.getDocument().getString("Message").trim();
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate("Timestamp"));
                    chatMessage.dateObject = documentChange.getDocument().getDate("Timestamp");
                    chatMessages.add(chatMessage);

                    //Notify Adapter that dataset has changed.
                    chatAdapter.notifyDataSetChanged();
                }
            }

            //Sort the Conversations according to oldest and recent.
            Collections.sort(chatMessages, (obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }

            //Notify Adapter and set scroll position.
            else{
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);

        if(conversationId == null){
            checkForConversation();
        }
    };

    /**
     * This method is to load the details of the Sender and the Receiver.
     */
    private void loadUserDetails(){
        //Getting Realtime Database instance
        mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        //Finding Receiver Username in Realtime Database through the Receiver Email Address
        mDatabase.child("Users").child(userEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    ReceiverUsername = String.valueOf(task.getResult().child("username").getValue());
                    user.setText(ReceiverUsername);
                }
            }
        });

        //Finding Sender Username in Realtime Database through the Sender Email Address
        mDatabase.child("Users").child(currentUserEmail.replace(".", "").trim()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //Set User Username to Textview
                else {
                    SenderUsername = String.valueOf(task.getResult().child("username").getValue());
                }
            }
        });
    }

    /**
     * This method is to set the respective listeners in the activity.
     */
    private void setListeners(){
        //If User click back, go back to previous activity.
        binding.imageBack.setOnClickListener(v -> onBackPressed());

        //If User click the Send button, call the Send message method.
        binding.layoutSend.setOnClickListener(v -> sendMessage());

        //If User click the info button, call the Check user profile method.
        binding.imageInfo.setOnClickListener(v -> checkUserProfile());
    }

    /**
     * This method is to get the formatted Date Time.
     */
    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    /**
     * This method is to add the conversation into the Firestore. This is shown as the last conversation with the user.
     * @param conversation -> The Conversation between users
     */
    private void addConversation(HashMap<String, Object> conversation){
        //Add conversation to Firestore database
        database.collection("Conversation")
                .add(conversation)
                .addOnSuccessListener(documentReference -> conversationId = documentReference.getId());
    }

    /**
     * This method is to update the existing/previous conversation with the new one.
     * @param message -> Message sent by either the Receiver or Sender
     */
    private void updateConversation(String message){
        DocumentReference documentReference =
                database.collection("Conversation").document(conversationId);
        documentReference.update(
                "LastMessage", message,
                "TimeStamp", new Date()
        );
    }

    /**
     * This method is to check for any existing conversations.
     */
    private void checkForConversation(){

        //If chat messages is not empty. Call the 2 respective methods.
        if (chatMessages.size() != 0){
            checkForConversationRemotely(
                    currentUserEmail,
                    userEmail
            );
            checkForConversationRemotely(
                    userEmail,
                    currentUserEmail
            );
        }
    }

    /**
     * This method is to check for any existing conversations.
     */
    private void checkForConversationRemotely(String senderId, String receiverId){
        //Check database for conversation
        database.collection("Conversation")
                .whereEqualTo("SenderEmail", senderId)
                .whereEqualTo("ReceiverEmail", receiverId)
                .get()
                .addOnCompleteListener(conversationOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversationOnCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0 ){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversationId = documentSnapshot.getId();
        }
    };

    /**
     * This method is to allow User to check the Receiver Profile page with their name and email displayed.
     */
    private void checkUserProfile(){
        //Intent to check the profile of the user you're texting with.
        Intent profileIntent = new Intent(ChatActivity.this, ChatUserProfile.class);
        profileIntent.putExtra("Name", ReceiverUsername);
        profileIntent.putExtra("Email", userEmail);
        startActivity(profileIntent);
    }

    /**
     * This method is to call the method to check if receiver is online when resuming the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        listenReceiverAvailability();
    }
}