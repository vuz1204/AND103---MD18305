package fpoly.vunvph33438.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import fpoly.vunvph33438.lab1.model.User;
import fpoly.vunvph33438.lab1.utils.FirebaseUtil;

public class SignUpActivity extends AppCompatActivity {
    EditText edEmailSU, edPasswordSU, edUsernameSU;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        edEmailSU = findViewById(R.id.edEmailSU);
        edPasswordSU = findViewById(R.id.edPasswordSU);
        edUsernameSU = findViewById(R.id.edUsernameSU);

        Button btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = edEmailSU.getText().toString().trim();
                String strPass = edPasswordSU.getText().toString().trim();
                String strUsername = edUsernameSU.getText().toString().trim();

                if (TextUtils.isEmpty(strUsername) || TextUtils.isEmpty(strEmail) || TextUtils.isEmpty(strPass)) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(strEmail, strPass)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("SignUpActivity", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUserIfNotExists(strEmail, strUsername);
                                    Toast.makeText(SignUpActivity.this, "Account created for " + user.getEmail(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                } else {
                                    Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void createUserIfNotExists(final String email, final String username) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        User newUser = new User();
                        newUser.setEmailOrPhoneNumber(email);
                        newUser.setUsername(username);
                        newUser.setUserId(FirebaseUtil.currentUserId());

                        FirebaseUtil.currentUserDetails().set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("SignUp", "User document created!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("SignUp", "Error creating user document", e);
                                    }
                                });
                    }
                } else {
                    Log.d("SignUp", "get failed with ", task.getException());
                }
            }
        });
    }
}
