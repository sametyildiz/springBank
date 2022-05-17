package com.springbank.users.customer.bankaccount.process.remittance;

import com.springbank.users.customer.bankaccount.Currency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@Getter
@Setter
public final class RemittanceRequest {
    private Long senderID;
    private Long receiverID;
    private String receiverName;
    private String receiverSurname;
    private Double amount;

    private Currency currency;


    public RemittanceRequest(Long senderID, Long receiverID, String receiverName, String receiverSurname, Double amount, Currency currency) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.receiverName = receiverName;
        this.receiverSurname = receiverSurname;
        this.amount = amount;
        this.currency = currency;
    }


}
