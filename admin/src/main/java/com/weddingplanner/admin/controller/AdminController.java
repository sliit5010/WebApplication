package com.weddingplanner.admin.controller;

import com.weddingplanner.admin.model.Admin;
import com.weddingplanner.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public String listAdmins(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admins/list";
    }

    @GetMapping("/add")
    public String addAdminForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admins/form";
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute Admin admin) {
        if (admin.getId() != null && !admin.getId().isEmpty()) {
            adminService.updateAdmin(admin);
        } else {
            adminService.saveAdmin(admin);
        }
        return "redirect:/admins";
    }

    @GetMapping("/edit/{id}")
    public String editAdminForm(@PathVariable String id, Model model) {
        model.addAttribute("admin", adminService.getAdminById(id));
        return "admins/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteAdmin(@PathVariable String id) {
        adminService.deleteAdmin(id);
        return "redirect:/admins";
    }
}
