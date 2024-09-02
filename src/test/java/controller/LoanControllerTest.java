package controller;

import com.example.constants.LoanStatus;
import com.example.controller.LoanController;
import com.example.model.Loan;
import com.example.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private LoanController loanController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
    }

    @Test
    void testApplyForLoan() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setUserId(1L);
        loan.setAmountRequired(10000.0);
        loan.setLoanTerm(3);

        when(loanService.applyForLoan(any(Loan.class),any(LocalDate.class), any())).thenReturn(loan);

        mockMvc.perform(post("/loans/apply")
                .contentType("application/json")
                .content("{\"amountRequired\": 10000.0, \"loanTerm\": 3}")
                .principal(() -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.amountRequired").value(10000.0))
                .andExpect(jsonPath("$.loanTerm").value(3));
    }

    @Test
    void testApproveLoan() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(LoanStatus.APPROVED);

        when(loanService.approveLoan(eq(1L))).thenReturn(loan);

        mockMvc.perform(put("/loans/approve/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testRepayLoan() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);

        when(loanService.repayLoan(eq(1L), eq(3333.33))).thenReturn(loan);

        mockMvc.perform(put("/loans/repay/1")
                .param("amount", "3333.33"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetLoans() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);
        List<Loan> loans = new ArrayList<>();
        loans.add(loan);

        when(loanService.getLoansByUserId(eq(1L))).thenReturn(loans);

        mockMvc.perform(get("/loans").principal(() -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}