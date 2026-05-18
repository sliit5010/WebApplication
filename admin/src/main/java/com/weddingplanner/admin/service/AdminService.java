package com.weddingplanner.admin.service;

import com.weddingplanner.admin.model.Admin;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final String FILE_PATH = "data/admins.txt";

    public AdminService() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists()) dataDir.mkdirs();
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                file.createNewFile();
                // Add a default SuperAdmin
                saveAdmin(new Admin(UUID.randomUUID().toString(), "admin", "admin123", "SuperAdmin"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    admins.add(new Admin(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return admins;
    }

    public Admin getAdminById(String id) {
        return getAllAdmins().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Admin getAdminByUsername(String username) {
        return getAllAdmins().stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void saveAdmin(Admin admin) {
        if (admin.getId() == null || admin.getId().isEmpty()) {
            admin.setId(UUID.randomUUID().toString());
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(admin.getId() + "|" + admin.getUsername() + "|" + admin.getPassword() + "|" + admin.getRole());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateAdmin(Admin admin) {
        List<Admin> admins = getAllAdmins();
        for (int i = 0; i < admins.size(); i++) {
            if (admins.get(i).getId().equals(admin.getId())) {
                admins.set(i, admin);
                break;
            }
        }
        rewriteFile(admins);
    }

    public void deleteAdmin(String id) {
        List<Admin> admins = getAllAdmins().stream()
                .filter(a -> !a.getId().equals(id))
                .collect(Collectors.toList());
        rewriteFile(admins);
    }

    private void rewriteFile(List<Admin> admins) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Admin a : admins) {
                writer.println(a.getId() + "|" + a.getUsername() + "|" + a.getPassword() + "|" + a.getRole());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
