package com.springbank.users.customer;

import com.springbank.security.Credentials;
import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.AccountService;
import com.springbank.users.customer.address.Address;
import com.springbank.users.customer.address.AddressService;
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

    private static int customerCount = 0;
    private Account account2;
    private Account account3;

    private Address address;


    @BeforeEach
    void setUp() {
        account = generateAccount();
        account2 = generateAccount();
        account3 = generateAccount();
        Credentials c1 = generateCredential();
        Credentials c2 = generateCredential();

        customer = generateCustomer();
        c1.setUser(customer);
        customer.setCredentials(c1);

        customer2 = generateCustomer();
        c2.setUser(customer2);
        customer2.setCredentials(c2);

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

    public Credentials generateCredential(){
        return new Credentials(){{
            setNId("11111111" + customerCount++);
            setPassword("1234");
        }};
    }
}