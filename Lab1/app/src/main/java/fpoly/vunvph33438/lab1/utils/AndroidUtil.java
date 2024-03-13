package fpoly.vunvph33438.lab1.utils;

import android.content.Intent;

import fpoly.vunvph33438.lab1.model.User;

public class AndroidUtil {
    public static void passUserModelAsIntent(Intent intent, User model) {
        intent.putExtra("username", model.getUsername());
        intent.putExtra("emailOrPhoneNumber", model.getEmailOrPhoneNumber());
        intent.putExtra("userId", model.getUserId());
    }

    public static User getUserModelFromIntent(Intent intent) {
        User user = new User();
        user.setUsername(intent.getStringExtra("username"));
        user.setEmailOrPhoneNumber(intent.getStringExtra("emailOrPhoneNumber"));
        user.setUserId(intent.getStringExtra("userId"));
        return user;
    }
}
