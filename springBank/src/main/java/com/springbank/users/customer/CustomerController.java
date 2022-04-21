package com.springbank.users.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/{id}/profile")
    public String getCustomer(Model model, @PathVariable("id") Long id) {
        Optional<Customer> customer = customerService.getCustomer(id);
        if(customer.isEmpty())
            log.error("Customer with id {} not found", id);

        model.addAttribute("customer", customer.get());
        return "customers/customer-profile";
    }



}