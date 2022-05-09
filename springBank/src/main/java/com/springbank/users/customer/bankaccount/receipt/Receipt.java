package com.springbank.users.customer.bankaccount.receipt;

import com.springbank.users.customer.bankaccount.Account;
import com.springbank.users.customer.bankaccount.Currency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.util.Calendar;
import java.util.Date;


@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Receipt {

    @Id
    private Long ID;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="sender_id", referencedColumnName = "ID")
    private Account sender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReceiptType type;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Currency currency;

    @PastOrPresent
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar processedDate;


}
enum ReceiptType {
    Remittance,CreditCardPayment,BillsPayment
}
