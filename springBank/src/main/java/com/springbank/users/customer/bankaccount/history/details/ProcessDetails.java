package com.springbank.users.customer.bankaccount.history.details;

import com.springbank.users.customer.bankaccount.Currency;
import com.springbank.users.customer.bankaccount.history.ProcessType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
@NoArgsConstructor
@Getter
@Setter
@Document
public class ProcessDetails {
    @Id
    private Long processId;

    private Long reciverId;

    private String reciverName;

    private ProcessType processType;

    private String CreditCardNumber;

    private Double amount;

    private Currency currency;

    public ProcessDetails(Long processId, Long reciverId,
                          String reciverName, ProcessType processType, Double amount, Currency currency) {
        this.processId = processId;
        this.reciverId = reciverId;
        this.reciverName = reciverName;
        this.processType = processType;
        this.amount = amount;
        this.currency = currency;
    }
}
