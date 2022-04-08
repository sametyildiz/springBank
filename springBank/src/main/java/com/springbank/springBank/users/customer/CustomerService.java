package com.springbank.springBank.users.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(timeout = 100)
    public Customer getCustomer(Long id) {
        return customerDAO.findById(id).orElse(null);
    }
    @Transactional(timeout = 100)
    public boolean deleteCustomer(Customer customer) {
        if(customer == null)
            return  false;
        customerDAO.delete(customer);
        return true;
    }

}
