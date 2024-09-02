package com.example.strategy;

import com.example.model.Loan;
import com.example.model.Repayment;
import com.example.strategy.RepaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class RepaymentContext {

    private final Map<String, RepaymentStrategy> strategyMap;

    @Autowired
    public RepaymentContext(Map<String, RepaymentStrategy> strategyMap) {
        this.strategyMap = strategyMap;
    }

    public List<Repayment> createRepaymentSchedule(String type, LocalDate startDate, Loan loan) {
        RepaymentStrategy strategy = strategyMap.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Invalid repayment type: " + type);
        }
        return strategy.createRepaymentSchedule(loan,startDate);
    }
}
