package fpoly.vunvph33438.lab1;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import fpoly.vunvph33438.lab1.model.User;
import fpoly.vunvph33438.lab1.utils.FirebaseUtil;

public class LoginByPhoneNumberActivity extends AppCompatActivity {

    private CountryCodePicker countryCodePicker;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private EditText otp;
    private EditText phoneNumber;
    private EditText username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone_number);

        mAuth = FirebaseAuth.getInstance();

        phoneNumber = findViewById(R.id.edPhoneNumber);
        username = findViewById(R.id.edUsernameByPhone);
        otp = findViewById(R.id.edOtp);
        countryCodePicker = findViewById(R.id.loginCountryCode);

        countryCodePicker.registerCarrierNumberEditText(phoneNumber);

        Button btnGetOtp = findViewById(R.id.btnGetOtp);
        btnGetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = countryCodePicker.getFullNumberWithPlus().trim();
                if (!phone.isEmpty()) {
                    getOTP(phone);
                } else {
                    Toast.makeText(LoginByPhoneNumberActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnLoginByPhoneNumber = findViewById(R.id.btnLoginByPhoneNumber);
        btnLoginByPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp.getText().toString().trim();
                final String phoneNumber = countryCodePicker.getFullNumberWithPlus().trim();
                final String username = ((EditText) findViewById(R.id.edUsernameByPhone)).getText().toString().trim();

                if (!code.isEmpty()) {
                    if (!username.isEmpty()) {
                        verifyOTP(code, phoneNumber, username);
                    } else {
                        Toast.makeText(LoginByPhoneNumberActivity.this, "Please enter your username", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginByPhoneNumberActivity.this, "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential, countryCodePicker.getFullNumberWithPlus().trim(), username.getText().toString());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginByPhoneNumberActivity.this, "Verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                mVerificationId = s;
                Toast.makeText(LoginByPhoneNumberActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(LoginByPhoneNumberActivity.this, "SMS retrieval timed out. Please try again.", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String code, final String phoneNumber, final String username) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential, phoneNumber, username);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, final String phoneNumber, final String username) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(LoginByPhoneNumberActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            createUserIfNotExists(phoneNumber, username);
                            startActivity(new Intent(LoginByPhoneNumberActivity.this, MainActivity.class));
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(LoginByPhoneNumberActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginByPhoneNumberActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void createUserIfNotExists(final String phoneNumber, final String username) {
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (!document.exists()) {
                        User newUser = new User();
                        newUser.setEmailOrPhoneNumber(phoneNumber);
                        newUser.setUsername(username);
                        newUser.setUserId(FirebaseUtil.currentUserId());

                        FirebaseUtil.currentUserDetails().set(newUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("loginByPhoneNumber", "User document created!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("loginByPhoneNumber", "Error creating user document", e);
                                    }
                                });
                    }
                } else {
                    Log.d("loginByPhoneNumber", "get failed with ", task.getException());
                }
            }
        });
    }
}
