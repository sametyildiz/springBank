package com.springbank.users.customer.bankaccount.process.remittance;

import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.AccountResponsePagginate;
import com.springbank.users.customer.bankaccount.AccountService;
import com.springbank.utils.InvalidInput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RemittanceService {
    private final AccountService accountService;
    private final Map<String, Pattern> patternsOfRemittanceInfo = new HashMap<>(){{
        put("accountID",Pattern.compile("^[0-9]{1,6}$"));
        put("receiverName",Pattern.compile("(^(([a-zA-Z]{2,50})(\\s?))+$)"));
        put("receiverSurname",Pattern.compile("^[a-zA-Z]{2,30}$"));
        put("amount",Pattern.compile("^[\\d]+(\\.\\d{1,2})?$"));
    }};

    @Transactional(timeout = 100)
    public Long authenticatedCustomerID(){
        return accountService.getAuthenticatedCustomerID();
    }
    @Transactional(timeout = 100)
    public boolean remittance(RemittanceRequest remittanceRequest){
        if(!checkRemittanceRequest(remittanceRequest)){
            return false;
        }
        accountService.updateBalanceByOutlow(remittanceRequest.getSenderID(), remittanceRequest.getAmount());
        accountService.updateBalanceByInlow(remittanceRequest.getReceiverID(),remittanceRequest.getAmount());

        return true;
    }
    @Transactional(timeout = 100)
    public Page<AccountResponsePagginate> getAccountsChoices(Long customerID, int page, int size){
        return accountService.getCustomerAccounts(customerID,page,size);
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public boolean checkRemittanceRequest(RemittanceRequest remittanceRequest){
        checkRemittanceRequestByRegex(remittanceRequest);
        Account sender = accountService.getAccount(remittanceRequest.getSenderID())
                .orElseThrow(()->new InvalidInput("Sender account does not exist"));
        Account receiver = accountService.getAccount(remittanceRequest.getReceiverID())
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
}
