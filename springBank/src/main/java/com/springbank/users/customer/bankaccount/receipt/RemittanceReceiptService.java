package com.springbank.users.customer.bankaccount.receipt;

import com.springbank.users.customer.bankaccount.AccountService;
import com.springbank.users.customer.bankaccount.RemittanceRequest;
import com.springbank.utils.InvalidInput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.TimeZone;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class RemittanceReceiptService {
    private final RemittanceReceiptDAO remittanceReceiptDAO;
    private final AccountService accountService;

    @Transactional(timeout = 100)
    public void  saveRemittanceReceipt(RemittanceRequest remittanceRequest) {
        RemittanceReceipt remittanceReceipt = new RemittanceReceipt();
        remittanceReceipt.setSender(accountService.getAccount(remittanceRequest.getSenderID()).get());
        remittanceReceipt.setReceiver(accountService.getAccount(remittanceRequest.getReceiverID()).get());
        remittanceReceipt.setType(ReceiptType.Remittance);
        remittanceReceipt.setAmount(remittanceRequest.getAmount());
        remittanceReceipt.setCurrency(remittanceReceipt.getSender().getCurrency());
        remittanceReceipt.setProcessedDate(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
    }

    @Transactional(timeout = 100,readOnly = true)
    public RemittanceReceipt getRemittanceReceipt(Long id) {
        return remittanceReceiptDAO.findById(id).orElseThrow(()->new InvalidInput("RemittanceReceipt not found"));
    }
}
