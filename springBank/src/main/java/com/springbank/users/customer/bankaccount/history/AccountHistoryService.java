package com.springbank.users.customer.bankaccount.history;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountHistoryService {
    private  final AccountHistoryDAO accountHistoryDAO;


}
