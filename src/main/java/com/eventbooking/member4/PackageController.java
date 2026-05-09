package com.eventbooking.member4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;

    @GetMapping
    public List<Package> getAllPackages(@RequestParam(required = false) String eventType) {
        if (eventType != null && !eventType.isEmpty()) {
            return packageService.getPackagesByEventType(eventType);
        }
        return packageService.getAllPackages();
    }

    @GetMapping("/{id}")
    public Package getPackage(@PathVariable String id) {
        return packageService.getPackageById(id);
    }

    @PostMapping
    public void addPackage(@RequestBody Package pkg) {
        packageService.addPackage(pkg);
    }

    @PutMapping("/{id}")
    public void updatePackage(@PathVariable String id, @RequestBody Package pkg) {
        pkg.setId(id);
        packageService.updatePackage(pkg);
    }

    @DeleteMapping("/{id}")
    public void deletePackage(@PathVariable String id) {
        packageService.deletePackage(id);
    }
}
