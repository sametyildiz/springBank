package com.springbank.users.customer.bankaccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface AccountDAO extends JpaRepository<Account, Long> {
    Page<Account> findAllByCustomer_ID(Long id, Pageable pageable);

    boolean existsByCustomer_IDAndCustomer_BankAccount_ID(Long customerID, Long bankAccountID);


    @Modifying
    @Query("update Account a SET  a.balance = a.balance + :amount where a.ID = :id")
    void updateBalanceByPayment( @Param("id") Long id,@Param("amount")double amount);


}
