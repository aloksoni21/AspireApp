package com.example.strategy;

import com.example.constants.RepaymentStatus;
import com.example.model.Loan;
import com.example.model.Repayment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("weekly")
public class WeeklyRepaymentStrategy implements RepaymentStrategy {

    @Override
    public List<Repayment> createRepaymentSchedule(Loan loan, LocalDate startDate) {
        List<Repayment> repayments = new ArrayList<>();
        double weeklyAmount = loan.getAmountRequired() / loan.getLoanTerm();
        double remainder = loan.getAmountRequired() % loan.getLoanTerm();
        
        for (int i = 0; i < loan.getLoanTerm(); i++) {
            Repayment repayment = new Repayment();
            repayment.setLoan(loan);
            repayment.setDueDate(startDate.plusWeeks(i));
            repayment.setAmount(i == loan.getLoanTerm() - 1 ? weeklyAmount + remainder : weeklyAmount);
            repayment.setStatus(RepaymentStatus.PENDING);
            repayments.add(repayment);
        }
        return repayments;
    }
}
