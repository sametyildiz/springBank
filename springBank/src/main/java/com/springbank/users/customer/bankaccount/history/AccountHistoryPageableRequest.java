package com.springbank.users.customer.bankaccount.history;

import com.springbank.users.customer.bankaccount.Currency;

import java.util.Calendar;
import java.util.Date;

public record AccountHistoryPageableRequest(Long accountID,Long processId, ProcessType processType, String processDate,
                                            Double amount, Currency currency){
}
