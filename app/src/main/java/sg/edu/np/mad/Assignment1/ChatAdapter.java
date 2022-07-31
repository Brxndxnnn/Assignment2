package sg.edu.np.mad.Assignment1;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import sg.edu.np.mad.Assignment1.databinding.ActivityChatBinding;
import sg.edu.np.mad.Assignment1.databinding.ItemContainerReceivedMessageBinding;
import sg.edu.np.mad.Assignment1.databinding.ItemContainerSentMessageBinding;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    //Initialising the variables
    private final List<ChatMessage> chatMessages;
    public final String receiverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> chatMessages, String receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //If message is sent, inflate the sent message layout
        if(viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }

        //If message is received, inflate the receive message layout
        else{
            return new ReceivedMessageViewHolder(
                    ItemContainerReceivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //If message is sent, set data to sentmessage viewholder
        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMessages.get(position));
        }
        //If message is received, set data to receivedmessage viewholder
        else{
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position)); //add in receiverProfileImage
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        //Get the view type, either the sent or received
        if(chatMessages.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }
        else{
            return VIEW_TYPE_RECEIVED;
        }
    }


    //Chat bubble for the messages sent
    static class SentMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        //Set the data in the bubble
        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message.trim());
            binding.textDateTime.setText(chatMessage.dateTime);
        }
    }

    //Chat bubble for the messages received
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        DatabaseReference mDatabase;
        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding){
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        //Set the data in the bubble
        void setData(ChatMessage chatMessage){
            binding.textMessage.setText(chatMessage.message.trim());
            binding.textDateTime.setText(chatMessage.dateTime);

            mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

            if (MainActivity.loggedInEmail.equals(chatMessage.receiverId)){
                //Finding Picture in Realtime Database through current User Email Address
                mDatabase.child("Users").child(chatMessage.senderId.replace(".", "").trim()).child("profilepicUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //Check if user has existing profile pic
                        if (task.getResult().exists()) {
                            Glide.with(itemView.getContext()).load(task.getResult().getValue()).into(binding.imageProfile);
                        }
                        //Set User Profile Pic to ImageView
                        else {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                    }
                });
            }
            else{
                //Finding Picture in Realtime Database through current User Email Address
                mDatabase.child("Users").child(chatMessage.receiverId.replace(".", "").trim()).child("profilepicUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        //Check if user has existing profile pic
                        if (task.getResult().exists()) {
                            Glide.with(itemView.getContext()).load(task.getResult().getValue()).into(binding.imageProfile);
                        }
                        //Set User Profile Pic to ImageView
                        else {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                    }
                });
            }

        }
    }

}
