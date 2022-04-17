package com.springbank.springBank.users.customer;

import com.springbank.springBank.users.bankaccount.AccountResponsePagginate;
import com.springbank.springBank.users.bankaccount.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CustomerService {
    private final CustomerDAO customerDAO;

    private final AccountService accountService;

    @Transactional(timeout = 100)
    public boolean addCustomer(Customer customer) {
        if(customer == null)
            return false;
        customerDAO.save(customer);
        return true;
    }
    @Transactional(timeout = 100)
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

    @Transactional(timeout = 100)
    public Page<AccountResponsePagginate> getCustomerAccounts(Long id, int page, int size) {
        Optional<Customer> customer = customerDAO.findById(id);
        if(customer.isEmpty())
            return null;
        return accountService.getAccountList(customer.get().getID(), page, size);
    }


}
