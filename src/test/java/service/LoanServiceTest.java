package service;

import com.example.constants.LoanStatus;
import com.example.constants.RepaymentStatus;
import com.example.constants.RepaymentType;
import com.example.model.Loan;
import com.example.model.Repayment;
import com.example.repository.LoanRepository;
import com.example.repository.RepaymentRepository;
import com.example.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private RepaymentRepository repaymentRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApplyForLoan() {
        Loan loan = new Loan();
        loan.setAmountRequired(10000.0);
        loan.setLoanTerm(3);

        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan savedLoan = loanService.applyForLoan(loan, LocalDate.now(), RepaymentType.WEEKLY);

        assertNotNull(savedLoan);
        assertEquals(LoanStatus.PENDING, savedLoan.getStatus());
        verify(loanRepository, times(1)).save(loan);
        verify(repaymentRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testApproveLoan() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setAmountRequired(10000.0);
        loan.setLoanTerm(3);
        loan.setStatus(LoanStatus.PENDING);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan approvedLoan = loanService.approveLoan(1L);

        assertNotNull(approvedLoan);
        assertEquals(LoanStatus.APPROVED, approvedLoan.getStatus());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void testRepayLoan() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setAmountRequired(10000.0);
        loan.setLoanTerm(3);
        loan.setStatus(LoanStatus.APPROVED);

        Repayment repayment1 = new Repayment();
        repayment1.setId(1L);
        repayment1.setAmount(3333.33);
        repayment1.setStatus(RepaymentStatus.PENDING);

        Repayment repayment2 = new Repayment();
        repayment2.setId(2L);
        repayment2.setAmount(3333.33);
        repayment2.setStatus(RepaymentStatus.PENDING);

        Repayment repayment3 = new Repayment();
        repayment3.setId(3L);
        repayment3.setAmount(3333.34);
        repayment3.setStatus(RepaymentStatus.PENDING);

        List<Repayment> repayments = Arrays.asList(repayment1, repayment2, repayment3);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(repaymentRepository.findByLoanId(1L)).thenReturn(repayments);
        when(repaymentRepository.save(any(Repayment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Loan repaidLoan = loanService.repayLoan(1L, 3333.33);

        assertNotNull(repaidLoan);
        assertEquals(LoanStatus.APPROVED, repaidLoan.getStatus());
        assertEquals(RepaymentStatus.PAID, repayment1.getStatus());
        assertEquals(RepaymentStatus.PENDING, repayment2.getStatus());
        verify(repaymentRepository, times(1)).save(repayment1);
        verify(loanRepository, never()).save(loan);
    }

    @Test
    void testGetLoansByUserId() {
        Loan loan1 = new Loan();
        loan1.setId(1L);
        loan1.setUserId(1L);

        Loan loan2 = new Loan();
        loan2.setId(2L);
        loan2.setUserId(1L);

        List<Loan> loans = Arrays.asList(loan1, loan2);

        when(loanRepository.findByUserId(1L)).thenReturn(loans);

        List<Loan> userLoans = loanService.getLoansByUserId(1L);

        assertNotNull(userLoans);
        assertEquals(2, userLoans.size());
        assertTrue(userLoans.contains(loan1));
        assertTrue(userLoans.contains(loan2));
    }

    @Test
    void testUpdateLoanStatusIfFullyPaid() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(LoanStatus.APPROVED);

        Repayment repayment1 = new Repayment();
        repayment1.setStatus(RepaymentStatus.PAID);

        Repayment repayment2 = new Repayment();
        repayment2.setStatus(RepaymentStatus.PAID);

        List<Repayment> repayments = Arrays.asList(repayment1, repayment2);

        when(repaymentRepository.findByLoanId(1L)).thenReturn(repayments);
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        loanService.updateLoanStatusIfFullyPaid(1L);

        assertEquals(LoanStatus.PAID, loan.getStatus());
        verify(loanRepository, times(1)).save(loan);
    }
}
