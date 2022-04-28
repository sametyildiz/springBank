package com.springbank.utils;

import javax.naming.AuthenticationException;

public class InvalidAuthentication extends AuthenticationException {
    public InvalidAuthentication(String message) {
        super(String.format("Invalid Authentication : %s", message));
    }
}
