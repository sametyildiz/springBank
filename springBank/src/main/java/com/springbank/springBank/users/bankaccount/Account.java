package com.springbank.springBank.users.bankaccount;

import com.springbank.springBank.users.customer.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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

    @Column(nullable = false)
    @ColumnDefault("0.00")
    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = " Varchar(5) default 'TRY'")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "customer_id" , referencedColumnName = "ID")
    private Customer customer;

}

enum Currency {
    EUR, USD, GBP, TRY
}
