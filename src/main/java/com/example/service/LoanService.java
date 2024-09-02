package com.example.service;


import com.example.constants.LoanStatus;
import com.example.constants.RepaymentStatus;
import com.example.constants.RepaymentType;
import com.example.exception.LoanNotApprovedException;
import com.example.exception.LoanNotFoundException;
import com.example.model.Loan;
import com.example.model.Repayment;
import com.example.repository.LoanRepository;
import com.example.repository.RepaymentRepository;
import com.example.strategy.RepaymentContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private RepaymentContext repaymentContext;

    public Loan applyForLoan(Loan loan, LocalDate startDate, RepaymentType repaymentType) {
        loan.setStatus(LoanStatus.PENDING);
        Loan savedLoan = loanRepository.save(loan);
        createScheduledRepayments(savedLoan, startDate, repaymentType);
        return savedLoan;
    }

    public Loan approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        loan.setStatus(LoanStatus.APPROVED);
        loan.setWeeklyRepaymentAmount(loan.getAmountRequired() / loan.getLoanTerm());
        return loanRepository.save(loan);
    }

    public Loan repayLoan(Long loanId, Double amount) {
        Loan loan = loanRepository.findById(loanId).orElseThrow();
        if (loan.getStatus() == LoanStatus.APPROVED) {
            List<Repayment> repayments = repaymentRepository.findByLoanId(loanId);
            for (Repayment repayment : repayments) {
                if (repayment.getStatus() == RepaymentStatus.PENDING && amount >= repayment.getAmount()) {
                    repayment.setStatus(RepaymentStatus.PAID);
                    amount -= repayment.getAmount();
                    repaymentRepository.save(repayment);
                }
            }
            updateLoanStatusIfFullyPaid(loanId);
            return loan;
        } else {
            throw new LoanNotApprovedException("Loan is not approved yet.");
        }
    }

    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    private void createScheduledRepayments(Loan loan,LocalDate startDate, RepaymentType repaymentType) {
        List<Repayment> repayments = repaymentContext.createRepaymentSchedule(repaymentType.name(),startDate, loan);
        repaymentRepository.saveAll(repayments);
    }

    public void updateLoanStatusIfFullyPaid(Long loanId) {
        List<Repayment> repayments = repaymentRepository.findByLoanId(loanId);
        boolean allPaid = repayments.stream().allMatch(r -> r.getStatus() == RepaymentStatus.PAID);
        if (allPaid) {
            Loan loan = loanRepository.findById(loanId).orElseThrow();
            loan.setStatus(LoanStatus.PAID);
            loanRepository.save(loan);
        }
    }
}
