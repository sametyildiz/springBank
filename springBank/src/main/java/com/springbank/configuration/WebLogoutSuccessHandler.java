package com.springbank.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Slf4j
public class WebLogoutSuccessHandler implements LogoutSuccessHandler {
    @Autowired
    private SessionRegistry sessionRegistry;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        if(authentication != null || authentication.getDetails() != null){
            removeAuthSession(authentication, sessionRegistry);
            request.getSession().invalidate();
        }
        response.sendRedirect("/welcome");


    }

    private void removeAuthSession(Authentication authentication, SessionRegistry sessionRegistry) {
//        List<SessionInformation> sessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
//        if (sessions.size() > 0) { // there is only 1 session allowed
//            log.debug("removing session {} from registry", sessions.get(0).getSessionId());
//            sessionRegistry.removeSessionInformation(sessions.get(0).getSessionId());
//        }
        Optional<SessionInformation> session = sessionRegistry.getAllSessions(authentication.getPrincipal(), false)
                .stream().findFirst();
        session.ifPresent(s -> sessionRegistry.removeSessionInformation(s.getSessionId()));
    }
}
