package com.example.strategy;

import com.example.model.Loan;
import com.example.model.Repayment;

import java.time.LocalDate;
import java.util.List;

public interface RepaymentStrategy {
    List<Repayment> createRepaymentSchedule(Loan loan, LocalDate startDate);
}
