package com.example.dto;

import lombok.Data;

@Data
public class LoanApplicationResponse {
    private Long loanId;
    private Boolean approved;

}
