package fpoly.vunvph33438.lab1.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import fpoly.vunvph33438.lab1.model.User;
import fpoly.vunvph33438.lab1.utils.AndroidUtil;
import fpoly.vunvph33438.lab1.utils.FirebaseUtil;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<User, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull User model) {
        try {
            holder.tvUsername.setText(model.getUsername());
            holder.tvEmailOrPhoneNumber.setText(model.getEmailOrPhoneNumber());

            if (model.getUserId().equals(FirebaseUtil.currentUserId())) {
                holder.tvUsername.setText(model.getUsername() + " (Me)");
            }

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                AndroidUtil.passUserModelAsIntent(intent, model);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        } catch (Exception e) {
            Log.e("AdapterError", "Error binding view holder at position: " + position, e);
        }
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmailOrPhoneNumber, tvUsername;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmailOrPhoneNumber = itemView.findViewById(R.id.tvEmailOrPhoneNumber);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }
}
