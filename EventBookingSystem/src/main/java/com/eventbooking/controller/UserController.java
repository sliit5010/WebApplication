package com.eventbooking.controller;

import com.eventbooking.model.PremiumUser;
import com.eventbooking.model.RegularUser;
import com.eventbooking.model.User;
import com.eventbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @ResponseBody
    public String registerUser(
            @RequestParam String fullName,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String userType) {

        User newUser;
        String generatedId = "U" + System.currentTimeMillis();

        if ("Premium".equals(userType)) {
            newUser = new PremiumUser(generatedId, fullName, email, password, "PREM10");
        } else {
            newUser = new RegularUser(generatedId, fullName, email, password);
        }

        userService.addUser(newUser);

        return "<h2>Success!</h2><p>User " + fullName + " registered successfully.</p><a href='/'>Go Back</a>";
    }
}
