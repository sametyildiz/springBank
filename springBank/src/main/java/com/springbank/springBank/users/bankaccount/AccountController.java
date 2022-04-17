package com.springbank.springBank.users.bankaccount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public String getAccount(Model model, @PathVariable("id") Long id) {
        Optional<Account> account = accountService.getAccount(id);
        if (account.isEmpty())
            log.error("Account with id {} not found", id);

        AccountResponse response =  account.map(m->
                        new AccountResponse(
                                m.getID(),
                                m.getBalance(),
                                m.getBranchCode(),
                                m.getCurrency(),
                                m.getCustomer().getID(),
                                m.getCustomer().getName(),
                                m.getCustomer().getSurname())).get();

        model.addAttribute("account", response);
        return "customers/customer-account";
    }

}
record AccountResponse(Long ID,double balance,int branchCode, Currency currency ,
                       long customerId, String customerName,String customerSurname){
}
