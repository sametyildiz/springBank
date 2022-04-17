package com.springbank.springBank.users.customer.address;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddressService {
    private final AddressDAO addressDAO;

    @Transactional(timeout = 100,readOnly = true)
    public Address getAddress(Long id) {
        return addressDAO.findById(id).orElse(null);
    }

    @Transactional(timeout = 100)
    public boolean addAddress(Address address){
        if(address == null)
            return false;
        addressDAO.save(address);
        return true;
    }
}
