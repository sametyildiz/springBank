package com.springbank.springBank.users.bankaccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<Account, Long> {
    Page<Account> findAllByCustomer_ID(Long id, Pageable pageable);

}
