package com.springbank.springBank.users.bankaccount;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

