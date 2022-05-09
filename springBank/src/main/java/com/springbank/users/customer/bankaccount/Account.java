package com.springbank.users.customer.bankaccount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springbank.users.customer.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_seq")
    @GenericGenerator(name = "bank_account_seq",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "bank_account_seq"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "100000"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            })
    @Column(name = "id",updatable = false, nullable = false)
    private Long ID;

    @Column(columnDefinition = "numeric default 0.0") // is working for primitive type
    private double balance;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Currency currency = Currency.TRY;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customerId" , referencedColumnName = "ID")
    private Customer customer;

    @Column(nullable = false)
    private int branchCode;

}


