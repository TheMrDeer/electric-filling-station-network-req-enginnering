package org.example;

public abstract class User {

    private String userId;
    private String name;
    private String email;
    private String password;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        setEmail(email);
        setPassword(password);
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            throw new IllegalArgumentException("Email is not valid");
        }
        this.email = email;
    }


    public void setPassword(String password) {
        int minLength = 8;
        if (password.length() < minLength) {
            throw new IllegalArgumentException("Password must have at least "+minLength+" characters");
        }
            this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void addUser() {
        UserManager.addUser(this);
    }

    public void register() {
        if (UserManager.findByEmail(this.getEmail()) != null) {
            throw new IllegalStateException("User already exists");
        }
        UserManager.addUser(this);
        System.out.println("User successfully registered: " + getEmail());
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void print() {
        System.out.printf("UserId: %s, Name: %s, Email: %s", this.userId, this.name, this.email);
    }

    public String getUserId() {
        return userId;
    }
}

