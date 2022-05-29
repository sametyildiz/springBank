package com.springbank.users.customer.bankaccount.history.details;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProcessDetailsDAO extends MongoRepository<ProcessDetails, Long> {

    Optional<ProcessDetails> findByProcessId(Long processId);


}
