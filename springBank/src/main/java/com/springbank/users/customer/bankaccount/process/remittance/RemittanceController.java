package com.springbank.users.customer.bankaccount.process.remittance;

import com.springbank.users.customer.bankaccount.AccountResponsePagginate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Slf4j
@Controller
@RequiredArgsConstructor
public class RemittanceController {
    private final RemittanceService remittanceService;

    @GetMapping("/remittance")
    public String remittanceRequest(Model model,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "3") Integer size) {
        Long customerID = remittanceService.authenticatedCustomerID();
        Page<AccountResponsePagginate> response = remittanceService.getAccountsChoices(customerID,page,size);

        model.addAttribute("accountList", response);
        model.addAttribute("remittance", new RemittanceRequest());

        return "customers/customer-account-remittance";

    }
    @PostMapping("/remittance")
    public String handleRemittanceRequest(@ModelAttribute("remittance") RemittanceRequest remittanceRequest){
        remittanceService.remittance(remittanceRequest);
        return "redirect:/account-options/info/" + remittanceRequest.getSenderID();

        //TODO: redirect to the receipt page--dekont
    }
}
