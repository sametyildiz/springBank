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
    private final Map<String, Pattern> patternsOfRemittanceInfo = new HashMap<>(){{
        put("accountID",Pattern.compile("^[0-9]{1,6}$"));
        put("receiverName",Pattern.compile("(^(([a-zA-Z]{2,50})(\\s?))+$)"));
        put("receiverSurname",Pattern.compile("^[a-zA-Z]{2,30}$"));
        put("amount",Pattern.compile("^[\\d]+(\\.\\d{1,2})?$"));
    }};
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
    public boolean remittance(RemittanceRequest remittanceRequest){
        if(!checkRemittanceRequest(remittanceRequest)){
            return false;
        }
        accountDAO.updateBalanceByPayment(remittanceRequest.getSenderID(),-remittanceRequest.getAmount());
        accountDAO.updateBalanceByPayment(remittanceRequest.getReceiverID(), remittanceRequest.getAmount());

        return true;
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
     public boolean checkRemittanceRequest(RemittanceRequest remittanceRequest){
        checkRemittanceRequestByRegex(remittanceRequest);
        Account sender = accountDAO.findById(remittanceRequest.getSenderID())
                .orElseThrow(()->new InvalidInput("Sender account does not exist"));
        Account receiver = accountDAO.findById(remittanceRequest.getReceiverID())
                .orElseThrow(()->new InvalidInput("Receiver account does not exist"));


        if(remittanceRequest.getAmount() <=0)
            throw new InvalidInput("Amount must be greater than 0");
        if(remittanceRequest.getSenderID().equals(remittanceRequest.getReceiverID())){
            throw new InvalidInput("Sender and receiver can't be the same");
        }
        if(sender.getBalance() < remittanceRequest.getAmount()){
            throw new InvalidInput("Sender account does not have enough balance");
        }
        if(!sender.getCurrency().equals(receiver.getCurrency())){
            throw new InvalidInput("Sender and receiver account must have the same currency");
        }
        if(receiver.getID() == remittanceRequest.getReceiverID()
            && receiver.getCustomer().getName().equalsIgnoreCase(remittanceRequest.getReceiverName())
            && receiver.getCustomer().getSurname().equalsIgnoreCase(remittanceRequest.getReceiverSurname()))
            return true;

        return false;
    }
    private void checkRemittanceRequestByRegex(RemittanceRequest remittanceRequest){
        trimRemittanceRequest(remittanceRequest);
        if(!patternsOfRemittanceInfo.get("accountID").matcher(remittanceRequest.getSenderID().toString()).matches())
            throw new InvalidInput("Sender account ID is not valid");
        if(!patternsOfRemittanceInfo.get("accountID").matcher(remittanceRequest.getReceiverID().toString()).matches())
            throw new InvalidInput("Receiver account ID is not valid");
        if(!patternsOfRemittanceInfo.get("amount").matcher(remittanceRequest.getAmount().toString()).matches())
            throw new InvalidInput("Amount is not valid");
        if(!patternsOfRemittanceInfo.get("receiverName").matcher(remittanceRequest.getReceiverName()).matches())
            throw new InvalidInput("Receiver name is not valid");
        if(!patternsOfRemittanceInfo.get("receiverSurname").matcher(remittanceRequest.getReceiverSurname()).matches())
            throw new InvalidInput("Receiver surname is not valid");
    }


    private void trimRemittanceRequest(RemittanceRequest remittanceRequest){
        if(remittanceRequest.getReceiverName() == null || remittanceRequest.getReceiverSurname() == null){
            throw new InvalidInput("Receiver name or surname is null");
        }
        remittanceRequest.setReceiverName(remittanceRequest.getReceiverName().trim());
        remittanceRequest.setReceiverSurname(remittanceRequest.getReceiverSurname().trim());
    }

    @Transactional(timeout = 100)
    public Page<AccountResponsePagginate> getCustomerAccounts(Long id, int page, int size) {
        Optional<Customer> customer = customerService.getCustomer(id);
        if(customer.isEmpty())
           throw new InvalidInput("Customer ID does not exist");
        return getAccountList(customer.get().getID(), page, size);
    }

    public Long getAuthenticatedCustomerID(){
        return credentialsService.getAuthenticatedCustomerID();
    }

}

