package com.springbank.springBank.users.customer;

import com.springbank.springBank.users.bankaccount.Account;
import com.springbank.springBank.users.bankaccount.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    private final Customer customer = new Customer();
    private final Account account = new Account();

    @BeforeEach
    void setUp() {
        customer.setName("Test");
        customer.setSurname("Test");
        customer.setPhone("123456789");
        customer.setTC("1111111111111111");

        account.setCustomer(customer);

    }

    @Test
    void addCustomer() {
        customerService.addCustomer(customer);
        assertEquals(customer.getID(), customerService.getCustomer(customer.getID()).getID());
        accountService.addAccount(account);
        assertEquals(account.getID(), accountService.getAccount(account.getID()).getID());
    }
}