package com.springbank.users.customer.bankaccount.receipt;

import com.springbank.users.customer.bankaccount.Account;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class RemittanceReceipt extends Receipt {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="receiver_id", referencedColumnName = "ID")
    private Account receiver;
}
