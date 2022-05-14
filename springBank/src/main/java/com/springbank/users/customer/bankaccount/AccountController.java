package com.springbank.users.customer.bankaccount;

import com.springbank.utils.InvalidAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/account-options")
public class AccountController {
    private final AccountService accountService;


    @GetMapping(value = "/info/{id}")
    public String getAccount(Model model, @PathVariable("id") Long id) throws InvalidAuthentication {
        Optional<Account> account = accountService.getAccountWithAuth(id);
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

    @GetMapping("/info/accountList")
    public String getCustomerAccount(Model model,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "3") Integer size) {
        Long id = accountService.getAuthenticatedCustomerID();
        Page<AccountResponsePagginate> response =
                accountService.getCustomerAccounts(id,page,size);
        model.addAttribute("accountList", response);

        return "customers/customer-account-list";
    }


}
record AccountResponse(Long ID,double balance,int branchCode, Currency currency ,
                       long customerId, String customerName,String customerSurname){
}
