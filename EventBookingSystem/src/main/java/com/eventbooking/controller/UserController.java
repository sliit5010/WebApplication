package com.eventbooking.controller;

import com.eventbooking.model.PremiumUser;
import com.eventbooking.model.RegularUser;
import com.eventbooking.model.User;
import com.eventbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for User Management.
 * Provides endpoints for Add, View All, View by ID, Update, and Delete.
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // ----------------------------------------------------------------
    // HOME — Serve the main index page
    // ----------------------------------------------------------------
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    // ----------------------------------------------------------------
    // ADD — POST /users/add
    // Accepts form fields: name, email, password, userType
    // ----------------------------------------------------------------
    @PostMapping("/users/add")
    @ResponseBody
    public String addUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String userType) {

        String generatedId = "U" + System.currentTimeMillis();
        User newUser;

        if ("Premium".equalsIgnoreCase(userType)) {
            // Premium users receive a discount code automatically
            newUser = new PremiumUser(generatedId, name, email, password, "PREM10");
        } else {
            newUser = new RegularUser(generatedId, name, email, password);
        }

        userService.addUser(newUser);

        return "{\"success\": true, \"message\": \"User '" + name + "' added successfully!\", \"userId\": \"" + generatedId + "\"}";
    }

    // ----------------------------------------------------------------
    // VIEW ALL — GET /users
    // Returns all users as a JSON array
    // ----------------------------------------------------------------
    @GetMapping("/users")
    @ResponseBody
    public String getAllUsers() {
        List<User> users = userService.getAllUsers();

        if (users.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            json.append("{")
                .append("\"userId\":\"").append(u.getUserId()).append("\",")
                .append("\"name\":\"").append(u.getName()).append("\",")
                .append("\"email\":\"").append(u.getEmail()).append("\",")
                .append("\"userType\":\"").append(u.getUserType()).append("\"");

            // Include discountCode for Premium users
            if (u instanceof PremiumUser) {
                json.append(",\"discountCode\":\"").append(((PremiumUser) u).getDiscountCode()).append("\"");
            }
            json.append("}");
            if (i < users.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    // ----------------------------------------------------------------
    // VIEW BY ID — GET /users/{id}
    // Returns a single user as JSON
    // ----------------------------------------------------------------
    @GetMapping("/users/{id}")
    @ResponseBody
    public String getUserById(@PathVariable String id) {
        User u = userService.getUserById(id);

        if (u == null) {
            return "{\"success\": false, \"message\": \"User not found.\"}";
        }

        StringBuilder json = new StringBuilder("{");
        json.append("\"userId\":\"").append(u.getUserId()).append("\",");
        json.append("\"name\":\"").append(u.getName()).append("\",");
        json.append("\"email\":\"").append(u.getEmail()).append("\",");
        json.append("\"userType\":\"").append(u.getUserType()).append("\"");
        if (u instanceof PremiumUser) {
            json.append(",\"discountCode\":\"").append(((PremiumUser) u).getDiscountCode()).append("\"");
        }
        json.append("}");
        return json.toString();
    }

    // ----------------------------------------------------------------
    // UPDATE — POST /users/update
    // Accepts: userId, name, email, password (optional)
    // ----------------------------------------------------------------
    @PostMapping("/users/update")
    @ResponseBody
    public String updateUser(
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam(required = false, defaultValue = "") String password) {

        boolean updated = userService.updateUser(userId, name, email, password);

        if (updated) {
            return "{\"success\": true, \"message\": \"User updated successfully.\"}";
        } else {
            return "{\"success\": false, \"message\": \"User not found.\"}";
        }
    }

    // ----------------------------------------------------------------
    // DELETE — POST /users/delete
    // Accepts: userId
    // ----------------------------------------------------------------
    @PostMapping("/users/delete")
    @ResponseBody
    public String deleteUser(@RequestParam String userId) {
        boolean deleted = userService.deleteUser(userId);

        if (deleted) {
            return "{\"success\": true, \"message\": \"User deleted successfully.\"}";
        } else {
            return "{\"success\": false, \"message\": \"User not found.\"}";
        }
    }
}