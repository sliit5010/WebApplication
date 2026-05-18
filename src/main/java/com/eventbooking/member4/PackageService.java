package com.eventbooking.member4;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PackageService {
    private final String FILE_PATH = "packages.txt";

    public PackageService() {
        // Initialize file if not exists and seed with some data
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                addPackage(new Package(UUID.randomUUID().toString(), "Standard Package", "Wedding", "1 Photographer, 4 Hours Coverage, 100 Edited Photos", 500.0));
                addPackage(new Package(UUID.randomUUID().toString(), "Premium Package", "Wedding", "2 Photographers, 1 Videographer, 8 Hours Coverage, 300 Edited Photos", 1200.0));
                addPackage(new Package(UUID.randomUUID().toString(), "Luxury Package", "Wedding", "2 Photographers, 2 Videographers, Full Day Coverage, Drone Footage, 500 Edited Photos", 2500.0));
                
                addPackage(new Package(UUID.randomUUID().toString(), "Standard Package", "Gathering", "1 Photographer, 3 Hours Coverage, 50 Edited Photos", 300.0));
                addPackage(new Package(UUID.randomUUID().toString(), "Premium Package", "Gathering", "1 Photographer, 1 Videographer, 5 Hours Coverage, 150 Edited Photos", 800.0));
                
                addPackage(new Package(UUID.randomUUID().toString(), "Standard Package", "Party", "1 Photographer, 4 Hours Coverage, 100 Edited Photos", 400.0));
                addPackage(new Package(UUID.randomUUID().toString(), "Luxury Package", "Party", "2 Photographers, 1 Videographer, 8 Hours Coverage, After-movie", 1500.0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Package> getAllPackages() {
        List<Package> packages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Package pkg = Package.fromString(line);
                if (pkg != null) packages.add(pkg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packages;
    }

    public List<Package> getPackagesByEventType(String eventType) {
        return getAllPackages().stream()
                .filter(p -> p.getEventType().equalsIgnoreCase(eventType))
                .toList();
    }

    public Package getPackageById(String id) {
        return getAllPackages().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addPackage(Package pkg) {
        if (pkg.getId() == null || pkg.getId().isEmpty()) {
            pkg.setId(UUID.randomUUID().toString());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(pkg.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePackage(Package updatedPkg) {
        List<Package> packages = getAllPackages();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Package pkg : packages) {
                if (pkg.getId().equals(updatedPkg.getId())) {
                    writer.write(updatedPkg.toString());
                } else {
                    writer.write(pkg.toString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deletePackage(String id) {
        List<Package> packages = getAllPackages();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Package pkg : packages) {
                if (!pkg.getId().equals(id)) {
                    writer.write(pkg.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
