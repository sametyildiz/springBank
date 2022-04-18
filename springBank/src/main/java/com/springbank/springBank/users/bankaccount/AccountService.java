package com.springbank.springBank.users.bankaccount;

import com.springbank.springBank.utils.InvalidInput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class AccountService {
    private final AccountDAO accountDAO;

    @Transactional(timeout = 100)
    public boolean addAccount(Account account) {
        if(account == null)
            return false;
        accountDAO.save(account);
        return true;
    }

    @Transactional(timeout = 100)
    public Optional<Account> getAccount(Long id) {
        return accountDAO.findById(id);
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

        return accountDAO.findAllByCustomer_ID(ID, PageRequest.of(page, pageSize)).map(
                m -> new AccountResponsePagginate(m.getID(),m.getBalance(),m.getBranchCode(),m.getCurrency()));
    }

    @Transactional(timeout = 100)
    public boolean remittance(RemittanceRequest remittanceRequest){
        if(!checkRemittanceRequest(remittanceRequest)){
            return false;
        }
        accountDAO.updateBalanceByPayment(remittanceRequest.senderID(),-remittanceRequest.amount());
        accountDAO.updateBalanceByPayment(remittanceRequest.receiverID(), remittanceRequest.amount());

        return true;
    }

    @Transactional(timeout = 100,readOnly = true, propagation = Propagation.MANDATORY)
     boolean checkRemittanceRequest(RemittanceRequest remittanceRequest){
        Account sender = accountDAO.findById(remittanceRequest.senderID())
                .orElseThrow(()->new InvalidInput("Sender account does not exist"));
        Account receiver = accountDAO.findById(remittanceRequest.receiverID())
                .orElseThrow(()->new InvalidInput("Receiver account does not exist"));
        if(remittanceRequest.amount() <=0)
            throw new InvalidInput("Amount must be greater than 0");
        if(remittanceRequest.senderID().equals(remittanceRequest.receiverID())){
            throw new InvalidInput("Sender and receiver can't be the same");
        }
        if(sender.getBalance() < remittanceRequest.amount()){
            throw new InvalidInput("Sender account does not have enough balance");
        }
        if(!sender.getCurrency().equals(receiver.getCurrency())){
            throw new InvalidInput("Sender and receiver account must have the same currency");
        }
        if(receiver.getID() == remittanceRequest.receiverID()
            && receiver.getCustomer().getName().equalsIgnoreCase(remittanceRequest.receiverName())
            && receiver.getCustomer().getSurname().equalsIgnoreCase(remittanceRequest.receiverSurname()))
            return true;

        return false;
    }

}

