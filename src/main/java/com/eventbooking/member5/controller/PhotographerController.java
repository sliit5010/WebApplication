package com.eventbooking.member5.controller;

import com.eventbooking.model.EventPhotographer;
import com.eventbooking.model.Photographer;
import com.eventbooking.model.WeddingPhotographer;
import com.eventbooking.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @PostMapping("/add-photographer")
    @ResponseBody
    public String addPhotographer(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String specialization) {

        Photographer newPhotographer;
        String generatedId = "P" + System.currentTimeMillis();

        // Implement inheritance logic based on dropdown selection
        if ("Wedding".equals(specialization)) {
            newPhotographer = new WeddingPhotographer(generatedId, name, email, phone, true);
        } else {
            newPhotographer = new EventPhotographer(generatedId, name, email, phone, false);
        }

        photographerService.addPhotographer(newPhotographer);

        return "<h2>Success!</h2><p>Photographer " + name + " added successfully.</p><a href='/photographer.html'>Go Back</a> | <a href='/User.html'>Home</a>";
    }
}
