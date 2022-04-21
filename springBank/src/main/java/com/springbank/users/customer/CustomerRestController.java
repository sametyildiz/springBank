package com.springbank.users.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/rest/customer")
public class CustomerRestController {
    private final CustomerService customerService;

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomer(id);
        if(customer.isEmpty()) {
            return null;
        }
        return customerService.getCustomer(id).get();
    }
}

