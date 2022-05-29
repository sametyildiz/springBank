package com.springbank.users.customer.bankaccount;

import com.springbank.security.Credentials;
import com.springbank.users.customer.Customer;
import com.springbank.users.customer.CustomerService;
import com.springbank.users.customer.bankaccount.process.remittance.RemittanceRequest;
import com.springbank.users.customer.bankaccount.process.remittance.RemittanceService;
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
    @Autowired
    private RemittanceService remittanceService;
    private static int customerCount = 0;
    private Customer c1 ,c2;

    private Credentials cd1 ,cd2;
    private Account a1 ,a2;
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
        assertDoesNotThrow(()->remittanceService.remittance(
                new RemittanceRequest(a1.getID(),a2.getID(),"Test","test",100.0 , Currency.TRY.toString())));
        assertDoesNotThrow(()->remittanceService.remittance(
                new RemittanceRequest(a1.getID(),a2.getID(),"test","test",100.0, Currency.TRY.toString())));
        assertFalse(remittanceService.remittance(
                new RemittanceRequest(a1.getID(),a2.getID(),"Tset","test",100.0, Currency.TRY.toString())));
    }

    private static Stream<Arguments> invalidRemittanceRequests(){
        return Stream.of(
                Arguments.of(new RemittanceRequest(100000L,100000L,"test","test",100.0, Currency.TRY.toString())
                        , IllegalArgumentException.class, "Invalid input: Sender and receiver can't be the same"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test","test",10000.0, Currency.TRY.toString())
                        , IllegalArgumentException.class , "Invalid input: Sender account does not have enough balance"),
                Arguments.of( new RemittanceRequest(10100000L,100001L,"Test","test",100.0, Currency.TRY.toString())
                        , IllegalArgumentException.class , "Invalid input: Sender account ID is not valid"),
                Arguments.of( new RemittanceRequest(100000L,20L,"Test","test",100.0, Currency.TRY.toString())
                        , IllegalArgumentException.class , "Invalid input: Receiver account does not exist"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test","test",0.0, Currency.TRY.toString())
                        , IllegalArgumentException.class , "Invalid input: Amount must be greater than 0"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test","test",-100.0, Currency.TRY.toString())
                        , IllegalArgumentException.class , "Invalid input: Amount is not valid"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test-2","test",100.0, Currency.TRY.toString()),
                        IllegalArgumentException.class , "Invalid input: Receiver name is not valid"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"test","te st",100.0, Currency.TRY.toString()),
                        IllegalArgumentException.class , "Invalid input: Receiver surname is not valid"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"test","test",-10000.0, Currency.TRY.toString()),
                        IllegalArgumentException.class , "Invalid input: Amount is not valid"),
                Arguments.of( new RemittanceRequest(100000L,100001L,"test","test",Double.valueOf("-000.1"), Currency.TRY.toString()),
                        IllegalArgumentException.class , "Invalid input: Amount is not valid"),
                Arguments.of( new RemittanceRequest(1000000L,100001L,"test","test",10000.0, Currency.TRY.toString()),
                        IllegalArgumentException.class , "Invalid input: Sender account ID is not valid")

        );
    }



/* propagation: mandatory
   @ParameterizedTest
    @MethodSource("invalidRemittanceRequests")
    void checkRemittanceRequest(RemittanceRequest request, Class<? extends Exception> exception , String message){
        Exception exceptionExpected = assertThrows((exception),()->accountService.checkRemittanceRequest(request));
        assertEquals(exceptionExpected.getMessage(),message);
    }*/

    private static Stream<Arguments> remittanceRequests(){
        return Stream.of(
                Arguments.of(new RemittanceRequest(100000L,100001L,"test","test",100.0, Currency.TRY.toString())
                        ,100,900),
                Arguments.of( new RemittanceRequest(100000L,100001L,"Test","test",550.5, Currency.TRY.toString())
                ,650.5,900-550.5)

        );
    }
    @ParameterizedTest
    @MethodSource("remittanceRequests")
    void checkRemittanceResult(RemittanceRequest request, double receiverBalance, double senderBalance){
        remittanceService.remittance(request);
        assertEquals(receiverBalance, accountService.getAccount(request.getReceiverID()).get().getBalance());
        assertEquals(senderBalance, accountService.getAccount(request.getSenderID()).get().getBalance());
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


   /* @Test
    void remittance() {
        double amount = 100;
        double senderBalance = accountService.getAccount(a1.getID()).get().getBalance();
        double reciverBalance = accountService.getAccount(a2.getID()).get().getBalance();
        RemittanceRequest request = new RemittanceRequest(a1.getID(),a2.getID(),"test","test",amount);
        accountService.remittance(request);
        assertEquals(accountService.getAccount(request.getSenderID()).get().getBalance() - amount,
                senderBalance);
        assertEquals(accountService.getAccount(request.getReceiverID()).get().getBalance() + amount,
                reciverBalance);
    }*/
}