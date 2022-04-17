package com.springbank.springBank.users.customer;

import com.springbank.springBank.users.customer.address.Address;
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
    private String national_ID;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Account> bankAccount;


    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id",referencedColumnName = "ID")
    private Address address;

}
