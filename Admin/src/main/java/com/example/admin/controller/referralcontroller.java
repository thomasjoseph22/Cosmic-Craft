package com.example.admin.controller;

import com.example.library.model.Customer;
import com.example.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class referralcontroller {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/displayCustomers")
    public String getCustomersByReferralCode(@RequestParam(name = "referralCode", required = false, defaultValue = "") String referralCode, Model model) {
        List<Customer> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("size", customers.size());
        return "displayCustomers";
    }
}
