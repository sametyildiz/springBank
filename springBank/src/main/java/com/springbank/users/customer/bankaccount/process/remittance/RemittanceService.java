package com.springbank.users.customer.bankaccount.process.remittance;

import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.AccountResponsePagginate;
import com.springbank.users.customer.bankaccount.AccountService;
import com.springbank.users.customer.bankaccount.history.AccountHistory;
import com.springbank.users.customer.bankaccount.history.ProcessType;
import com.springbank.users.customer.bankaccount.history.details.ProcessDetails;
import com.springbank.utils.InvalidInput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        saveProcess(remittanceRequest);
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
        if( remittanceRequest.getCurrency() != sender.getCurrency() || !sender.getCurrency().equals(receiver.getCurrency())){
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

    @Transactional(timeout = 100, propagation = Propagation.MANDATORY)
    public void saveProcess(RemittanceRequest remittanceRequest){
        Map.Entry<AccountHistory,AccountHistory> entry = createProcessHistory(remittanceRequest);
        ProcessDetails details = createProcessDetails(remittanceRequest);
        AccountHistory senderHistory = accountService.saveProcess(entry.getKey());
        accountService.saveProcess(entry.getValue());

        details.setProcessId(senderHistory.getProcessId());
        accountService.saveProcessDetails(details);
    }
    private Map.Entry<AccountHistory,AccountHistory> createProcessHistory(RemittanceRequest remittanceRequest){
        Calendar calendar = Calendar.getInstance();
        AccountHistory senderHistory = generateHistory(
                accountService.getAccount(remittanceRequest.getSenderID()).get(),
                ProcessType.Outflow,
                remittanceRequest,
                calendar);
        AccountHistory receiverHistory = generateHistory(
                accountService.getAccount(remittanceRequest.getReceiverID()).get(),
                ProcessType.Inflow,
                remittanceRequest,
                calendar);
        return new AbstractMap.SimpleEntry<>(senderHistory,receiverHistory);
    }

    private AccountHistory generateHistory(Account account, ProcessType processType, RemittanceRequest remittanceRequest, Calendar calendar){
        AccountHistory history = new AccountHistory();
        history.setAccount(account);
        history.setProcessType(processType);
        history.setProcessDateTime(calendar);
        history.setAmount(remittanceRequest.getAmount());
        history.setCurrency(remittanceRequest.getCurrency());
        return history;
    }
    private ProcessDetails createProcessDetails(RemittanceRequest remittanceRequest){
        ProcessDetails processDetails = new ProcessDetails();
        processDetails.setProcessType(ProcessType.Transfer);
        processDetails.setReciverId(remittanceRequest.getReceiverID());
        processDetails.setReciverName(String.format("%s %s",
                remittanceRequest.getReceiverName(),remittanceRequest.getReceiverSurname()));
        processDetails.setDescription(remittanceRequest.getDescription());

        return processDetails;
    }
}
