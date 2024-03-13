package fpoly.vunvph33438.lab1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import fpoly.vunvph33438.lab1.ChatActivity;
import fpoly.vunvph33438.lab1.R;
import fpoly.vunvph33438.lab1.model.Chatroom;
import fpoly.vunvph33438.lab1.model.User;
import fpoly.vunvph33438.lab1.utils.AndroidUtil;
import fpoly.vunvph33438.lab1.utils.FirebaseUtil;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<Chatroom, RecentChatRecyclerAdapter.ChatroomModelViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Chatroom> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull Chatroom model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId());

                        User otherUserModel = task.getResult().toObject(User.class);
                        holder.tvUsername.setText(otherUserModel.getUsername());
                        if (lastMessageSentByMe) {
                            holder.tvLastMessage.setText("You: " + model.getLastMessage());
                        } else {
                            holder.tvLastMessage.setText(model.getLastMessage());
                        }
                        holder.tvLastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimeStamp()));

                        holder.itemView.setOnClickListener(v -> {
                            Intent intent = new Intent(context, ChatActivity.class);
                            AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        });
                    }
                });
    }

    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatroomModelViewHolder(view);
    }

    class ChatroomModelViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvLastMessage, tvLastMessageTime;

        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
        }
    }
}
