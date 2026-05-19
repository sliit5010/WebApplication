package com.photo.controller;

import com.photo.model.PhotographerDTO;
import com.photo.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photographers")
public class PhotographerController {

    @Autowired
    private PhotographerService service;

    @GetMapping
    public List<PhotographerDTO> getAll() {
        return service.getAll();
    }

    @PostMapping
    public void add(@RequestBody PhotographerDTO dto) {
        service.add(dto);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable String id, @RequestBody PhotographerDTO dto) {
        service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
