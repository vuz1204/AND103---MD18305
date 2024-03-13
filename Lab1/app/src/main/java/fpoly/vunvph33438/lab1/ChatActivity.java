package fpoly.vunvph33438.lab1;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.Arrays;

import fpoly.vunvph33438.lab1.adapter.ChatRecyclerAdapter;
import fpoly.vunvph33438.lab1.model.ChatMessage;
import fpoly.vunvph33438.lab1.model.Chatroom;
import fpoly.vunvph33438.lab1.model.User;
import fpoly.vunvph33438.lab1.utils.AndroidUtil;
import fpoly.vunvph33438.lab1.utils.FirebaseUtil;

public class ChatActivity extends AppCompatActivity {

    User otherUser;
    String chatroomId;
    Chatroom chatroom;
    ChatRecyclerAdapter adapter;

    EditText messageInput;
    ImageButton sendMessageBtn, backButton;
    TextView otherUsername;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chatMessageInput);
        sendMessageBtn = findViewById(R.id.messageSendBtn);
        backButton = findViewById(R.id.backBtn);
        otherUsername = findViewById(R.id.otherUsername);
        recyclerView = findViewById(R.id.chatRecyclerView);

        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        otherUsername.setText(otherUser.getUsername());

        sendMessageBtn.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty()) {
                return;
            }
            sendMessageToUser(message);
        });

        getOrCreateChatroomModel();
        setUpChatRecyclerView();
    }

    void setUpChatRecyclerView() {
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class).build();

        adapter = new ChatRecyclerAdapter(options, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    void sendMessageToUser(String message) {
        chatroom.setLastMessageTimeStamp(Timestamp.now());
        chatroom.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroom.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatroom);

        ChatMessage chatMessage = new ChatMessage(message, FirebaseUtil.currentUserId(), Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessage)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            messageInput.setText("");
                        }
                    }
                });
    }

    void getOrCreateChatroomModel() {
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    chatroom = task.getResult().toObject(Chatroom.class);
                    if (chatroom == null) {
                        chatroom = new Chatroom(
                                chatroomId,
                                Arrays.asList(FirebaseUtil.currentUserId(), otherUser.getUserId()),
                                Timestamp.now(),
                                ""
                        );
                        FirebaseUtil.getChatroomReference(chatroomId).set(chatroom);
                    }
                }
            }
        });
    }
}