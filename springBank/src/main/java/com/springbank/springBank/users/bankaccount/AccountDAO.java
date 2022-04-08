package com.springbank.springBank.users.bankaccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDAO extends JpaRepository<Account, Long> {
}
