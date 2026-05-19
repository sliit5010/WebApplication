package com.weddingplanner.admin.controller;

import com.weddingplanner.admin.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/income")
    public String viewIncome(Model model) {
        model.addAttribute("totalIncome", incomeService.getTotalIncome());
        model.addAttribute("incomeByMonth", incomeService.getIncomeByMonth());
        model.addAttribute("recentPayments", incomeService.getRecentPayments());
        return "income";
    }
}
