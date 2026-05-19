package com.eventbooking.service;

import com.eventbooking.model.Photographer;
import org.springframework.stereotype.Service;
import java.io.*;

@Service
public class PhotographerService {
    private final String FILE_NAME = "photographers.txt";

    // CREATE Operation
    public void addPhotographer(Photographer photographer) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(photographer.toFileFormat());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to photographers.txt: " + e.getMessage());
        }
    }
}
