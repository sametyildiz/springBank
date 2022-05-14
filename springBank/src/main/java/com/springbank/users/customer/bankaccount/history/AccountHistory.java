package com.springbank.users.customer.bankaccount.history;

import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.util.Calendar;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class AccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="account_id", referencedColumnName = "ID")
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountProcessType processType;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Currency currency;

    @PastOrPresent
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar processedDate;

    @Column(length = 1000)
    private String description;

}
