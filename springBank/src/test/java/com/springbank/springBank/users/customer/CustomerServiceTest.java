package com.springbank.springBank.users.customer;

import com.springbank.springBank.users.bankaccount.Account;
import com.springbank.springBank.users.bankaccount.AccountService;
import com.springbank.springBank.users.customer.address.Address;
import com.springbank.springBank.users.customer.address.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class CustomerServiceTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AddressService addressService;
    private Customer customer;
    private Customer customer2;
    private Account account;

    private Account account2;
    private Account account3;

    private Address address;


    @BeforeEach
    void setUp() {
        account = generateAccount();
        account2 = generateAccount();
        account3 = generateAccount();

        customer = generateCustomer();
        customer2 = generateCustomer();

        account.setCustomer(customer);
        account3.setCustomer(customer);
        customer.setBankAccount(Set.of(account, account3));

        account2.setCustomer(customer2);
        customer2.setBankAccount(Set.of(account2));

        address = generateAddress();
        customer.setAddress(address);

    }

    @Test
    void addAddress() {
        customerService.addCustomer(customer);
   //     addressService.addAddress(address);
        assertNotNull(addressService.getAddress(customer.getAddress().getID()));
    }

    @Test
    void addCustomer() {
        customerService.addCustomer(customer);
        customerService.addCustomer(customer2);
        assertEquals(customer.getID(), customerService.getCustomer(customer.getID()).get().getID());
        //accountService.addAccount(account);
        assertEquals(account.getID(), accountService.getAccount(account.getID()).get().getID());
        assertEquals(0.0, accountService.getAccount(account.getID()).get().getBalance());
    }

    private Customer generateCustomer(){
        Customer customer = new Customer();
        customer.setName("Test");
        customer.setSurname("Test");
        customer.setPhone("123456789");
        customer.setEmail("test@mail.com");
        customer.setNational_ID("1111111111111111");
        return customer;
    }

    private Account generateAccount(){
        Account account = new Account();
        account.setBranchCode(1000);
        return account;
    }

    private Address generateAddress(){
        Address address = new Address();
        address.setCity("Ankara");
        address.setCountry("Turkey");
        address.setStreet("Istanbul");
        address.setZipCode("34000");
        address.setDetails("");
        return address;
    }
}