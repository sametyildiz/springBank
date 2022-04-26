package com.springbank.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityRestController {

    private final CredentialsService credentialsService;

    @GetMapping("/security/user")
    public AuthenticatedUser getUser() {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();

        Long id = credentialsService.getUserId(currentUser.getName()).orElse(0L);
        return new AuthenticatedUser(
                currentUser.getName(),
                id,
                currentUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toUnmodifiableSet())
        );
    }

}
record AuthenticatedUser(String username, Long id, Set<String> authorities){}
