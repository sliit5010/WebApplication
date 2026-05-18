package com.weddingplanner.admin.service;

import com.weddingplanner.admin.model.Photographer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotographerService {
    private final String FILE_PATH = "data/photographers.txt";

    public PhotographerService() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
                // Add default photographers matching the screenshot
                savePhotographer(new Photographer("P100", "Matheesh Perera", "0778508778", 11, 2.0, "Wedding Photographer", "Gold"));
                savePhotographer(new Photographer("P102", "WANNAKUWATTA WADUGE MATHEESHA DESHAN PERERA", "0778508773", 3, 15.0, "Event Photographer", "Silver"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Photographer> getAllPhotographers() {
        List<Photographer> photographers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 7) {
                    photographers.add(new Photographer(
                        parts[0], 
                        parts[1], 
                        parts[2], 
                        Integer.parseInt(parts[3]), 
                        Double.parseDouble(parts[4]), 
                        parts[5], 
                        parts[6]
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return photographers;
    }

    public Photographer getPhotographerById(String id) {
        return getAllPhotographers().stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void savePhotographer(Photographer photographer) {
        List<Photographer> photographers = getAllPhotographers();
        boolean exists = false;
        for (int i = 0; i < photographers.size(); i++) {
            if (photographers.get(i).getId().equalsIgnoreCase(photographer.getId())) {
                photographers.set(i, photographer);
                exists = true;
                break;
            }
        }
        if (!exists) {
            photographers.add(photographer);
        }
        rewriteFile(photographers);
    }

    public void deletePhotographer(String id) {
        List<Photographer> photographers = getAllPhotographers().stream()
                .filter(p -> !p.getId().equalsIgnoreCase(id))
                .collect(Collectors.toList());
        rewriteFile(photographers);
    }

    private void rewriteFile(List<Photographer> photographers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Photographer p : photographers) {
                writer.println(p.getId() + "|" + p.getFullName() + "|" + p.getContactNumber() + "|" +
                        p.getExperience() + "|" + p.getRate() + "|" + p.getSpecialization() + "|" + p.getPackageType());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
