package com.springbank.springBank.users.bankaccount;

public record RemittanceRequest(Long senderID, Long receiverID, String receiverName, String receiverSurname, Double amount){
}
