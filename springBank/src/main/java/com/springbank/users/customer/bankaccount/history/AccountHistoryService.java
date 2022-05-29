package com.springbank.users.customer.bankaccount.history;

import com.springbank.users.customer.bankaccount.history.details.ProcessDetails;
import com.springbank.users.customer.bankaccount.history.details.ProcessDetailsService;
import com.springbank.utils.LocalDateFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountHistoryService {
    private  final AccountHistoryDAO accountHistoryDAO;
    private final ProcessDetailsService processDetailsService;

    @Transactional(timeout = 100)
    public boolean saveProccess(AccountHistory accountHistory) {
        if(accountHistory != null) {
            accountHistoryDAO.save(accountHistory);
            return true;
        }
        return false;
    }
    @Transactional(timeout = 100)
    public AccountHistory saveAndGetProccess(AccountHistory accountHistory) {
        if(accountHistory != null) {

            return accountHistoryDAO.saveAndFlush(accountHistory);
        }
        return accountHistory;
    }

    @Transactional(timeout = 100)
    public boolean saveProcessDetails(ProcessDetails processDetails) {
        if(processDetails != null) {
            processDetailsService.save(processDetails);
            return true;
        }
        return false;
    }
    @Transactional(timeout = 100)
    public Page<AccountHistoryPageableRequest> getHistoryByAccountId(Long accountId, PageRequest pageRequest) {
        return accountHistoryDAO.findHistoriesByAccount_ID_OrderByProcessDateTimeDesc(accountId, pageRequest).map(
                h -> new AccountHistoryPageableRequest(
                        accountId, h.getProcessId(), h.getProcessType(), LocalDateFormat.localDate(h.getProcessDateTime()), h.getAmount(), h.getCurrency())
        );
    }
    @Transactional(timeout = 100)
    public List<AccountHistory> getHistoryByAccountId(Long accountId) {
        return accountHistoryDAO.findAllByAccount_ID(accountId);
    }
    @Transactional(timeout = 100)
    public ProcessDetails getProcessDetailsProcessId(Long processId) {
        return processDetailsService.getProcessDetails(processId);
    }
    @Transactional(timeout = 100)
    public boolean removeProcessDetails() {
        return processDetailsService.removeAll();
    }


}
