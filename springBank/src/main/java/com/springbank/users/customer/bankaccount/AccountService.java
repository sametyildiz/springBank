package com.springbank.users.customer.bankaccount;

import com.springbank.security.CredentialsService;
import com.springbank.users.customer.Customer;
import com.springbank.users.customer.CustomerService;
import com.springbank.utils.InvalidAuthentication;
import com.springbank.utils.InvalidInput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AccountService {
    private final AccountDAO accountDAO;
    private final CustomerService customerService;
    private final CredentialsService credentialsService;

    @Transactional(timeout = 100)
    public boolean saveAccount(Account account) {
        if(account == null)
            return false;
        accountDAO.save(account);
        return true;
    }

    @Transactional(timeout = 100)
    public Optional<Account> getAccount(Long id) {
        return accountDAO.findById(id);
    }

    /*TODO: How to do in security config*/
    @Transactional(timeout = 100,readOnly = true)
    public Optional<Account> getAccountWithAuth(Long accountID) throws InvalidAuthentication {
        if(!isAuthenticated(accountID))
            throw new InvalidAuthentication("You are not authorized to access this bank account");
        return accountDAO.findById(accountID);
    }

    @Transactional(timeout = 100,readOnly = true,propagation = Propagation.MANDATORY)
    public boolean isAuthenticated(Long accountID){
        Long customerID = credentialsService.getAuthenticatedCustomerID();
        return accountDAO.existsByCustomer_IDAndCustomer_BankAccount_ID(customerID,accountID);
    }

    @Transactional(timeout = 100)
    public boolean deleteAccount(Account account) {
        if(account == null)
            return false;
        accountDAO.delete(account);
        return true;
    }

    @Transactional(timeout = 100,readOnly = true)
    public Page<AccountResponsePagginate> getAccountList(Long ID, int page, int pageSize){

        return accountDAO.findAllByCustomer_IDOrderByIDAsc(ID, PageRequest.of(page, pageSize)).map(
                m -> new AccountResponsePagginate(m.getID(),m.getBalance(),m.getBranchCode(),m.getCurrency()));
    }

    @Transactional(timeout = 100)
    public Page<AccountResponsePagginate> getCustomerAccounts(Long id, int page, int size) {
        Optional<Customer> customer = customerService.getCustomer(id);
        if(customer.isEmpty())
           throw new InvalidInput("Customer ID does not exist");
        return getAccountList(customer.get().getID(), page, size);
    }
    @Transactional(timeout = 100)
    public void updateBalanceByInlow(Long id, Double amount) {
        accountDAO.updateBalanceByPayment(id, amount);
    }
    @Transactional(timeout = 100)
    public void updateBalanceByOutlow(Long id, Double amount) {
        accountDAO.updateBalanceByPayment(id, -amount);
    }

    public Long getAuthenticatedCustomerID(){
        return credentialsService.getAuthenticatedCustomerID();
    }

}

