package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import sg.edu.np.mad.Assignment1.databinding.ItemContainerRecentConversationBinding;

public class RecentConversationAdapter extends RecyclerView.Adapter<RecentConversationAdapter.ConversationViewHolder> {

    private final List<ChatMessage> chatMessages;

    public RecentConversationAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ItemContainerRecentConversationBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(chatMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder{

        DatabaseReference mDatabase;
        ItemContainerRecentConversationBinding binding;

        ConversationViewHolder(ItemContainerRecentConversationBinding itemContainerRecentConversationBinding){
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(ChatMessage chatMessage){
            //binding.imageProfile.setImageDrawable("@drawable/");
            binding.textName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);

            mDatabase = FirebaseDatabase.getInstance("https://mad-assignment-1-7b524-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

            if (MainActivity.loggedInEmail.equals(chatMessages.get(getBindingAdapterPosition()).receiverId)){
                //Finding Picture in Realtime Database through current User Email Address
                mDatabase.child("Users").child(chatMessages.get(getBindingAdapterPosition()).senderId.replace(".", "").trim()).child("profilepicUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                mDatabase.child("Users").child(chatMessages.get(getBindingAdapterPosition()).receiverId.replace(".", "").trim()).child("profilepicUrl").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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

            binding.getRoot().setOnClickListener(v -> {

                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                if(!chatMessages.get(getBindingAdapterPosition()).receiverId.equals(MainActivity.loggedInEmail)){
                    intent.putExtra("Name", chatMessages.get(getBindingAdapterPosition()).receiverId); //**************
                    intent.putExtra("ID", chatMessages.get(getBindingAdapterPosition()).conversationId); //**************
                    v.getContext().startActivity(intent);
                }
                else {
                    intent.putExtra("Name", chatMessages.get(getBindingAdapterPosition()).senderId); //**************
                    intent.putExtra("ID", chatMessages.get(getBindingAdapterPosition()).conversationId); //**************
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
