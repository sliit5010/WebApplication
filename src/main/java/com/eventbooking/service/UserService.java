package com.eventbooking.service;

import com.eventbooking.model.PremiumUser;
import com.eventbooking.model.RegularUser;
import com.eventbooking.model.User;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class handling all User CRUD operations.
 * Data is persisted to a flat file: users.txt
 * Each line format: userId,name,email,password,userType[,discountCode]
 */
@Service
public class UserService {

    private static final String FILE_NAME = "users.txt";

    // ----------------------------------------------------------------
    // ADD — Write a new user to users.txt
    // ----------------------------------------------------------------
    public void addUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(user.toFileFormat());
            writer.newLine();
            System.out.println("User added: " + user.getName());
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // VIEW ALL — Read and return all users from users.txt
    // ----------------------------------------------------------------
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                User user = parseLine(line);
                if (user != null) users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }
        return users;
    }

    // ----------------------------------------------------------------
    // VIEW BY ID — Find a single user by their userId
    // ----------------------------------------------------------------
    public User getUserById(String userId) {
        for (User user : getAllUsers()) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null; // Not found
    }

    // ----------------------------------------------------------------
    // UPDATE — Replace a user's name, email, and password by userId
    // ----------------------------------------------------------------
    public boolean updateUser(String userId, String newName, String newEmail, String newPassword) {
        List<User> users = getAllUsers();
        boolean found = false;

        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                user.setName(newName);
                user.setEmail(newEmail);
                if (newPassword != null && !newPassword.isBlank()) {
                    user.setPassword(newPassword);
                }
                found = true;
                break;
            }
        }

        if (found) {
            rewriteFile(users);
        }
        return found;
    }

    // ----------------------------------------------------------------
    // DELETE — Remove a user by userId
    // ----------------------------------------------------------------
    public boolean deleteUser(String userId) {
        List<User> users = getAllUsers();
        boolean removed = users.removeIf(user -> user.getUserId().equals(userId));
        if (removed) {
            rewriteFile(users);
        }
        return removed;
    }

    // ----------------------------------------------------------------
    // HELPERS
    // ----------------------------------------------------------------

    /**
     * Parses a CSV line from users.txt back into a User object.
     * Regular:  userId,name,email,password,Regular
     * Premium:  userId,name,email,password,Premium,discountCode
     */
    private User parseLine(String line) {
        try {
            String[] parts = line.split(",", -1);
            String userId   = parts[0];
            String name     = parts[1];
            String email    = parts[2];
            String password = parts[3];
            String userType = parts[4];

            if ("Premium".equalsIgnoreCase(userType)) {
                String discountCode = parts.length > 5 ? parts[5] : "";
                return new PremiumUser(userId, name, email, password, discountCode);
            } else {
                return new RegularUser(userId, name, email, password);
            }
        } catch (Exception e) {
            System.out.println("Skipping malformed line: " + line);
            return null;
        }
    }

    /**
     * Rewrites the entire users.txt file from the in-memory list.
     * Used by Update and Delete operations.
     */
    private void rewriteFile(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, false))) {
            for (User user : users) {
                writer.write(user.toFileFormat());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error rewriting file: " + e.getMessage());
        }
    }
}