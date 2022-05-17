package com.springbank.users.customer.bankaccount.process.remittance;

import com.springbank.security.Credentials;
import com.springbank.users.customer.Customer;
import com.springbank.users.customer.CustomerService;
import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.AccountService;
import com.springbank.users.customer.bankaccount.history.AccountHistory;
import com.springbank.users.customer.bankaccount.history.AccountHistoryService;
import com.springbank.users.customer.bankaccount.history.ProcessType;
import com.springbank.users.customer.bankaccount.history.details.ProcessDetails;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RemittanceServiceTest {
    @Autowired
    private  RemittanceService remittanceService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountHistoryService accountHistorySerivice;
    @Autowired
    private AccountService accountService;
    private Customer c1 ,c2;
    private Credentials cd1 ,cd2;
    private Account a1 ,a2;
    private RemittanceRequest request;
    private static int customerCount = 0;

    @BeforeEach
    void setUp() {

        cd1 = generateCredential();
        c1 = generateCustomer();
        cd2 = generateCredential();
        c2 = generateCustomer();

        a1 = generateAccount();
        a2 = generateAccount();

        cd1.setUser(c1);
        cd2.setUser(c2);

        c1.setCredentials(cd1);
        c2.setCredentials(cd2);

        a1.setBalance(1000);

        a1.setCustomer(c1);
        a2.setCustomer(c2);

        c1.setBankAccount(Set.of(a1));
        c2.setBankAccount(Set.of(a2));


        customerService.addCustomer(c1);
        customerService.addCustomer(c2);

        request = new RemittanceRequest();
        request.setSenderID(a1.getID());
        request.setReceiverID(a2.getID());

        request.setReceiverName(c1.getName());
        request.setReceiverSurname(c1.getSurname());

        request.setAmount(100D);
        request.setCurrency(a1.getCurrency());

    }
    @AfterEach
    void tearDown() {
        accountHistorySerivice.removeProcessDetails();

    }

  /*mandatory
    @Test
    void saveProcess() {
        remittanceService.saveProcess(request);
    }*/

    @Test
    void transferWithProcessHistoryCheckBalance(){
        double balance1 = a1.getBalance();
        double balance2 = a2.getBalance();
        remittanceService.remittance(request);
        assertEquals(balance1,accountService.getAccount(a1.getID()).get().getBalance() + request.getAmount());
        assertEquals(accountService.getAccount(a2.getID()).get().getBalance() - balance2, request.getAmount());
    }

    @Test
    void transferWithProcessHistoryCheckHistory(){
        remittanceService.remittance(request);
        List<AccountHistory> history = accountService.getAllAccountHistory(a1.getID());
        assertTrue(history.size() > 0);
        assertEquals(history.get(0).getAccount().getID(), a1.getID());
        assertEquals(history.get(0).getProcessType(), ProcessType.Outflow);
        assertTrue(history.get(0).getProcessDateTime().before(Calendar.getInstance()));
    }

    @Test
    void transferWithProcessHistoryCheckProcessDetails(){
        remittanceService.remittance(request);
        AccountHistory history = accountService.getAllAccountHistory(a1.getID()).get(0);
        ProcessDetails details = accountService.getProcessDetails(history.getProcessId());
        assertNotNull(details);
        assertEquals(details.getProcessType(), ProcessType.Transfer);
        assertEquals(details.getReciverId(), request.getReceiverID());
        assertEquals(details.getCurrency(), request.getCurrency());
        assertEquals(details.getAmount(), request.getAmount());
        assertEquals(details.getReciverName(), request.getReceiverName() + " " + request.getReceiverSurname());


    }


    private Customer generateCustomer(){

        Customer customer = new Customer();
        customer.setName("Test");
        customer.setSurname("test");
        customer.setPhone("123456789");
        customer.setEmail("test@mail.com");

        return customer;
    }
    private Credentials generateCredential(){
        return new Credentials(){{
            setNId("11111111" + customerCount++);
            setPassword("1234");
        }};
    }
    private Account generateAccount(){
        Account account = new Account();
        account.setBranchCode(1000);
        return account;
    }
}