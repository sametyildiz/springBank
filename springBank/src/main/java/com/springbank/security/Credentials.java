package com.springbank.security;

import com.springbank.users.customer.Customer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Credentials {

    @Id
    @Column
    private String nid;

    @Column
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customer_id", referencedColumnName = "ID")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean enabled;
}
