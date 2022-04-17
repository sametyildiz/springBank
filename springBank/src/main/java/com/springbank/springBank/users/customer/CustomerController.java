package com.springbank.springBank.users.customer;

import com.springbank.springBank.users.bankaccount.AccountResponsePagginate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    //to do: make account list
    @GetMapping("/{id}/accountList")
    public String getCustomerAccount(Model model,@PathVariable("id") Long id,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "3") Integer size) {
        Page<AccountResponsePagginate> response = customerService.getCustomerAccounts(id,page,size);

        model.addAttribute("accountList", response);

        return "customers/customer-account-list";
    }



}