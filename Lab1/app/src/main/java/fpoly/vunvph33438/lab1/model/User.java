package fpoly.vunvph33438.lab1.model;

public class User {
    private String emailOrPhoneNumber;
    private String username;
    private String userId;

    public User() {
    }

    public User(String emailOrPhoneNumber, String username, String userId) {
        this.emailOrPhoneNumber = emailOrPhoneNumber;
        this.username = username;
        this.userId = userId;
    }

    public String getEmailOrPhoneNumber() {
        return emailOrPhoneNumber;
    }

    public void setEmailOrPhoneNumber(String emailOrPhoneNumber) {
        this.emailOrPhoneNumber = emailOrPhoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
