package com.springbank.users.customer.bankaccount.history.details;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessDetailsService {
    private final ProcessDetailsDAO processDetailsDAO;

    @Transactional(readOnly = true,timeout = 100)
    public ProcessDetails getProcessDetails(Long processId) {
        return processDetailsDAO.findByProcessId(processId).orElse(null);
    }

    @Transactional(timeout = 100)
    public boolean save(ProcessDetails processDetails) {
        if(processDetails == null)
            return false;
        processDetailsDAO.save(processDetails);
        return true;
    }

    @Transactional(timeout = 100)
    public boolean remove(Long processId) {
        if(processId == null)
            return false;
        processDetailsDAO.deleteById(processId);
        return true;
    }

    @Transactional(timeout = 100)
    public boolean removeAll() {
        processDetailsDAO.deleteAll();
        return true;
    }
}
