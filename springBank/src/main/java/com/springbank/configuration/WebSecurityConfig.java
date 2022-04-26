package com.springbank.configuration;

import com.springbank.security.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select n_id, password, enabled from credentials where n_id=?")
                .authoritiesByUsernameQuery("select n_id, role from roles where n_id=?");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/welcome" ,"/security/user").permitAll()
                .antMatchers("/account-options/info/**").hasAnyRole(Role.User.name(),Role.Manager.name())
                    .antMatchers("/css/**","/js/**","/jq/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/auth/login")
                    .usernameParameter("nid")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/welcome")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler(createLogoutSuccessHandler())
                .and()
                    .sessionManagement()
                    .maximumSessions(1).maxSessionsPreventsLogin(true)
                    .expiredUrl("/welcome");

    }
    @Bean
    SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
    @Bean
    public LogoutSuccessHandler createLogoutSuccessHandler(){
        return new WebLogoutSuccessHandler();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean//removed Prefix "ROLE_"
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }
}
