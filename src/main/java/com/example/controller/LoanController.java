package com.example.controller;

import com.example.dto.LoanApplicationRequest;
import com.example.model.Loan;
import com.example.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public Loan applyForLoan(@RequestBody LoanApplicationRequest loanRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Loan loan = new Loan();
        loan.setUserId(getUserIdFromPrincipal(userDetails));
        loan.setAmountRequired(loanRequest.getAmountRequired());
        loan.setLoanTerm(loanRequest.getLoanTerm());
        return loanService.applyForLoan(loan, loanRequest.getStartDate(), loanRequest.getRepaymentType());
    }

    @PutMapping("/approve/{loanId}")
    public Loan approveLoan(@PathVariable Long loanId) {
        return loanService.approveLoan(loanId);
    }

    @PutMapping("/repay/{loanId}")
    public Loan repayLoan(@PathVariable Long loanId, @RequestParam Double amount) {
        return loanService.repayLoan(loanId, amount);
    }

    @GetMapping
    public List<Loan> getLoans(@AuthenticationPrincipal UserDetails userDetails) {
        return loanService.getLoansByUserId(getUserIdFromPrincipal(userDetails));
    }

    private Long getUserIdFromPrincipal(UserDetails userDetails) {
        // Simulate getting the user ID from the principal
        return 1L;
    }
}
