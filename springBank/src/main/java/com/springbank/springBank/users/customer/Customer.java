package com.springbank.springBank.users.customer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.springbank.springBank.users.bankaccount.Account;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @Column(nullable = false)
    private String TC;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Account> bankAccount;



}
