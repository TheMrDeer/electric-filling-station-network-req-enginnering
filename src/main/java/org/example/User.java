package org.example;

public abstract class User {

    private String userId;
    private String name;
    private String email;
    private String password;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void register() {
        if (UserRepository.findByEmail(this.getEmail()) != null) {
            throw new IllegalStateException("User already exists");
        }
        UserRepository.addUser(this);
        System.out.println("User successfully registered: " + getEmail());
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    public void print(){
        System.out.printf("UserId: %s, Name: %s, Email: %s",this.userId,this.name,this.email);
    }

    public String getUserId() {
        return userId;
    }
}

