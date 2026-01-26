package org.example;

import java.util.ArrayList;
import java.util.List;

public class UserManager {

    private static List<User> users = new ArrayList<>();

    // CRUD: User (create)
    public static User createUser(User user) {
        addUser(user);
        return user;
    }

    public static void addUser(User user) {
        if (getCustomerById(user.getUserId()) != null) {
            throw new IllegalStateException("User already exists");
        }
        users.add(user);
    }

    // CRUD: User (read)
    public static User getUserById(String userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    public static List<User> listUsers() {
        return users;
    }

    public static void removeUser(User user) {
        users.remove(user);
    }

    // CRUD: User (delete)
    public static void deleteUserById(String userId) {
        users.removeIf(u -> u.getUserId().equals(userId));
    }

    // CRUD: User (update)
    public static void updateUserEmail(String userId, String newEmail) {
        User user = getUserById(userId);
        if (user != null) {
            user.setEmail(newEmail);
        }
    }

    public static void updateUserPassword(String userId, String newPassword) {
        User user = getUserById(userId);
        if (user != null) {
            user.setPassword(newPassword);
        }
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
