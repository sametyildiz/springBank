package com.springbank.security;

import com.springbank.users.User;
import com.springbank.users.customer.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class CredentialsService {
    private final CredentialsDAO credentialsDAO;

    @Transactional(timeout = 100)
    public boolean saveCredential(Credentials credentials){
        if(credentials == null)
            return false;
        credentialsDAO.save(credentials);
        return true;
    }

    @Transactional(timeout = 100)
    public Optional<Long> getUserId(String username){
        return credentialsDAO.findById(username).map(Credentials::getUser).map(User::getID);
    }

    public Long getAuthenticatedCustomerID(){
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        return getUserId(currentUser.getName()).orElse(0L);
    }
}
