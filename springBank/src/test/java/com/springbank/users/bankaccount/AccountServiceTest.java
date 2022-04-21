package com.springbank.users.bankaccount;

import com.springbank.security.Credentials;
import com.springbank.users.customer.Customer;
import com.springbank.users.customer.CustomerService;
import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.AccountService;
import com.springbank.users.customer.bankaccount.RemittanceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    private static int customerCount = 0;
    private Customer c1;
    private Customer c2;

    private Credentials cd1;
    private Credentials cd2;
    private Account a1;
    private Account a2;
    @BeforeEach
    void setUp() {
        cd1 = generateCredential();
        c1 = generateCustomer();
        cd2 = generateCredential();
        c2 = generateCustomer();

        a1 = generateAccount();
        a2 = generateAccount();

        cd1.setCustomer(c1);
        cd2.setCustomer(c2);

        c1.setCredentials(cd1);
        c2.setCredentials(cd2);

        a1.setBalance(1000);

        a1.setCustomer(c1);
        a2.setCustomer(c2);

        c1.setBankAccount(Set.of(a1));
        c2.setBankAccount(Set.of(a2));


        customerService.addCustomer(c1);
        customerService.addCustomer(c2);
    }

    @Test
    void accountCheck() {
        assertEquals(1000,accountService.getAccount(a1.getID())
                .get().getBalance());
        assertEquals(0,accountService.getAccount(
                a2.getID())
                .get().getBalance());
    }
    @Test
    void remittanceNameEquals(){
        assertDoesNotThrow(()->accountService.remittance(
                new RemittanceRequest(a1.getID(),a2.getID(),"Test-2","test",100.0)));
        assertDoesNotThrow(()->accountService.remittance(
                new RemittanceRequest(a1.getID(),a2.getID(),"test-2","test",100.0)));
        assertFalse(accountService.remittance(
                new RemittanceRequest(a1.getID(),a2.getID(),"Tset-2","test",100.0)));
    }

    private static Stream<Arguments> invalidRemittanceRequests(){
        return Stream.of(
                Arguments.of(new RemittanceRequest(100000L,100000L,"test-2","test",100.0)
                        , IllegalArgumentException.class, "Invalid input: Sender and receiver can't be the same"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test-2","test",10000.0)
                        , IllegalArgumentException.class , "Invalid input: Sender account does not have enough balance"),
                Arguments.of( new RemittanceRequest(10100000L,100001L,"Test-2","test",100.0)
                        , IllegalArgumentException.class , "Invalid input: Sender account does not exist"),
                Arguments.of( new RemittanceRequest(100000L,20L,"Test-2","test",100.0)
                        , IllegalArgumentException.class , "Invalid input: Receiver account does not exist"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test-2","test",0.0)
                        , IllegalArgumentException.class , "Invalid input: Amount must be greater than 0"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test-2","test",-100.0)
                        , IllegalArgumentException.class , "Invalid input: Amount must be greater than 0")

        );
    }



    @ParameterizedTest
    @MethodSource("invalidRemittanceRequests")
    void checkRemittanceRequest(RemittanceRequest request, Class<? extends Exception> exception , String message){
        Exception exceptionExpected = assertThrows((exception),()->accountService.checkRemittanceRequest(request));
        assertEquals(exceptionExpected.getMessage(),message);
    }

    private static Stream<Arguments> remittanceRequests(){
        return Stream.of(
                Arguments.of(new RemittanceRequest(100000L,100001L,"test-2","test",100.0)
                        ,100,900),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test-2","test",550.5)
                ,650.5,900-550.5)

        );
    }
    @ParameterizedTest
    @MethodSource("remittanceRequests")
    void checkRemittanceResult(RemittanceRequest request, double receiverBalance, double senderBalance){
        accountService.remittance(request);
        assertEquals(receiverBalance, accountService.getAccount(request.getReceiverID()).get().getBalance());
        assertEquals(senderBalance, accountService.getAccount(request.getSenderID()).get().getBalance());
    }

    private Customer generateCustomer(){

        Customer customer = new Customer();
        customer.setName("Test-" +customerCount);
        customer.setSurname("test");
        customer.setPhone("123456789");
        customer.setEmail("test@mail.com");

        return customer;
    }

    public Credentials generateCredential(){
        return new Credentials(){{
            setNid("11111111" + customerCount++);
            setPassword("1234");
        }};
    }
    private Account generateAccount(){
        Account account = new Account();
        account.setBranchCode(1000);
        return account;
    }
}