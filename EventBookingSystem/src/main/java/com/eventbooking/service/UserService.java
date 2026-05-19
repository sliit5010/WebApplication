package com.eventbooking.service;

import com.eventbooking.model.User;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class UserService {
    private final String FILE_NAME = "users.txt";

    public void addUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(user.toFileFormat());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to users.txt: " + e.getMessage());
        }
    }
}
