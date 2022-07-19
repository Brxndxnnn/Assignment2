package sg.edu.np.mad.Assignment1;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        ItemContainerRecentConversationBinding binding;

        ConversationViewHolder(ItemContainerRecentConversationBinding itemContainerRecentConversationBinding){
            super(itemContainerRecentConversationBinding.getRoot());
            binding = itemContainerRecentConversationBinding;
        }

        void setData(ChatMessage chatMessage){
            //binding.imageProfile.setImageDrawable("@drawable/");
            binding.textName.setText(chatMessage.conversationName);
            binding.textRecentMessage.setText(chatMessage.message);
            //***********************************************
            binding.getRoot().setOnClickListener(v -> {
                String currentUserEmail;
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                currentUserEmail = currentUser.getEmail();

                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                if(!chatMessages.get(getAdapterPosition()).receiverId.equals(currentUserEmail)){
                    intent.putExtra("Name", chatMessages.get(getAdapterPosition()).receiverId); //**************
                    v.getContext().startActivity(intent);
                }
                else {
                    intent.putExtra("Name", chatMessages.get(getAdapterPosition()).senderId); //**************
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
