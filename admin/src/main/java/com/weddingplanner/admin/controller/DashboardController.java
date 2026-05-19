package com.weddingplanner.admin.controller;

import com.weddingplanner.admin.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalIncome", incomeService.getTotalIncome());
        model.addAttribute("recentPayments", incomeService.getRecentPayments());
        return "dashboard";
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/dashboard";
    }
}
