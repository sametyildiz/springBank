package com.springbank.users.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CustomerService {
    private final CustomerDAO customerDAO;

    @Transactional(timeout = 100)
    public boolean addCustomer(Customer customer) {
        if(customer == null)
            return false;
        customerDAO.save(customer);
        return true;
    }
    @Transactional(timeout = 100,readOnly = true)
    public Optional<Customer> getCustomer(Long id) {
        return customerDAO.findById(id);
    }
    @Transactional(timeout = 100)
    public boolean deleteCustomer(Customer customer) {
        if(customer == null)
            return  false;
        customerDAO.delete(customer);
        return true;
    }







}
