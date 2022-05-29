package com.springbank.users.customer.bankaccount.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountHistoryDAO extends JpaRepository<AccountHistory, Long> {

    List<AccountHistory> findAllByAccount_ID(Long accountId);
    Page<AccountHistory> findHistoriesByAccount_ID_OrderByProcessDateTimeDesc(Long accountId, Pageable pageable);
}
