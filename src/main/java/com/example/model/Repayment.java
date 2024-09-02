package com.example.model;

import com.example.constants.RepaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Data
public class Repayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
    private LocalDate dueDate;
    private Double amount;
    @Enumerated(EnumType.STRING)
    private RepaymentStatus status;

}
