package com.example.dto;

import com.example.constants.RepaymentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanApplicationRequest {
    private Double amountRequired;
    private Integer loanTerm;
    private LocalDate startDate;
    private RepaymentType repaymentType;
}
