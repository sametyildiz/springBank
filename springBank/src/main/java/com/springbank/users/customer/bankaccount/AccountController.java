package com.springbank.users.customer.bankaccount;

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
@RequestMapping("/transaction")
public class AccountController {
    private final AccountService accountService;

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
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

    @GetMapping("{id}/accountList")
    public String getCustomerAccount(Model model ,@PathVariable("id") Long id,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "3") Integer size) {
        Page<AccountResponsePagginate> response =
                accountService.getCustomerAccounts(id,page,size);

        model.addAttribute("accountList", response);

        return "customers/customer-account-list";
    }

//    @GetMapping("/{id}/remittance")
//    public String remittanceRequest(Model model,@PathVariable("id") Long id,
//                                    @RequestParam(defaultValue = "0") Integer page,
//                                    @RequestParam(defaultValue = "3") Integer size) {
//        Page<AccountResponsePagginate> response = accountService.getCustomerAccounts(id,page,size);
//
//        model.addAttribute("accountList", response);
//        model.addAttribute("remittance", new RemittanceRequest());
//
//        return "customers/customer-account-remittance";
//
//    }
//    @PostMapping("/{id}/remittance")
//    public String handleRemitanceRequest(@ModelAttribute("remittance") RemittanceRequest remittanceRequest){
//        accountService.remittance(remittanceRequest);
//        return "redirect:/transaction/{id}/accountList";
//    }
}
record AccountResponse(Long ID,double balance,int branchCode, Currency currency ,
                       long customerId, String customerName,String customerSurname){
}
