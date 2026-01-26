package org.example;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        if (getCustomerById(user.getUserId()) != null) {
            throw new IllegalStateException("User already exists");
        }
        users.add(user);
    }

    public static void removeUser(User user) {
        users.remove(user);
    }

    public static void clearUsers() {
        users.clear();
    }

    public static User findByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public static Customer getCustomerById(String userId){
        return (Customer) users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}
