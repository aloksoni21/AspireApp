package com.example.model;

import com.example.constants.LoanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Double amountRequired;
    private Integer loanTerm;
    private Double weeklyRepaymentAmount;

    private LoanStatus status;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Repayment> repayments;

}
