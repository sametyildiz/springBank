package com.springbank.users.customer;

import com.springbank.security.Credentials;
import com.springbank.users.User;
import com.springbank.users.customer.address.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.springbank.users.customer.bankaccount.Account;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "customer")
@Getter
@Setter
@NoArgsConstructor
public class Customer extends User {


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Account> bankAccount;
}
