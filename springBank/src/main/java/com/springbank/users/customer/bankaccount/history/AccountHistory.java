package com.springbank.users.customer.bankaccount.history;

import com.springbank.users.customer.bankaccount.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class AccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long processId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProcessType processType;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar processDateTime;
    @ManyToOne
    @JoinColumn(name = "account_id" , referencedColumnName = "ID")
    private Account account;

}
