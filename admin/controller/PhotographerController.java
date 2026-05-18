package com.weddingplanner.admin.controller;

import com.weddingplanner.admin.model.Photographer;
import com.weddingplanner.admin.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/photographers")
public class PhotographerController {

    @Autowired
    private PhotographerService photographerService;

    @GetMapping
    public String listPhotographers(Model model, @RequestParam(required = false) String editId) {
        model.addAttribute("photographers", photographerService.getAllPhotographers());
        
        Photographer formPhotographer = new Photographer();
        if (editId != null && !editId.isEmpty()) {
            Photographer existing = photographerService.getPhotographerById(editId);
            if (existing != null) {
                formPhotographer = existing;
            }
        }
        model.addAttribute("photographer", formPhotographer);
        model.addAttribute("editMode", editId != null && !editId.isEmpty());
        return "photographers";
    }

    @PostMapping("/save")
    public String savePhotographer(@ModelAttribute Photographer photographer) {
        if (photographer.getId() != null && !photographer.getId().isEmpty()) {
            photographerService.savePhotographer(photographer);
        }
        return "redirect:/photographers";
    }

    @GetMapping("/delete/{id}")
    public String deletePhotographer(@PathVariable String id) {
        photographerService.deletePhotographer(id);
        return "redirect:/photographers";
    }
}
