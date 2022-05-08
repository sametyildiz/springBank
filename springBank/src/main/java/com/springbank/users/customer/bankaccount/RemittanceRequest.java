package com.springbank.users.customer.bankaccount;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
@NoArgsConstructor
@Getter
@Setter
public final class RemittanceRequest {
    private Long senderID;
    private Long receiverID;
    private String receiverName;
    private String receiverSurname;
    private Double amount;


    public RemittanceRequest(Long senderID, Long receiverID, String receiverName, String receiverSurname, Double amount) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.receiverName = receiverName;
        this.receiverSurname = receiverSurname;
        this.amount = amount;
    }


}
