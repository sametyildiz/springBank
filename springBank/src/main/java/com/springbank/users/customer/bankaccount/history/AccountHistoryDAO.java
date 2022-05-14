package com.springbank.users.customer.bankaccount.history;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryDAO extends JpaRepository<AccountHistory, Long> {
}
