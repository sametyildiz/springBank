package com.springbank.users.customer.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbank.users.User;
import com.springbank.users.customer.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;


    @Column(nullable = false,length = 50)
    private String city;

    @Column(nullable = false,length = 100)
    private String street;

    @Column(nullable = false,length = 50)
    private String country;

    @Column(nullable = false,length = 20)
    private String zipCode;

    @Column
    private String details;

    @JsonIgnore
    @OneToOne(mappedBy = "address" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;
}
