package com.springbank.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsDAO extends JpaRepository<Credentials, String> {
}
