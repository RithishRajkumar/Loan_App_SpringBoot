package com.example.college.loan_application.service;

import com.example.college.loan_application.model.Emi;
import com.example.college.loan_application.model.LoanApplication;
import com.example.college.loan_application.model.LoanProduct;
import com.example.college.loan_application.model.LoanState;
import com.example.college.loan_application.repository.EmiRepository;
import com.example.college.loan_application.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanRepository;

    @Autowired
    private EmiRepository emiRepository;

    public LoanApplication createApplication(LoanApplication app) {
        app.setState(LoanState.DRAFT);
        return loanRepository.save(app);
    }

    public List<LoanApplication> getAllApplications() {
        return loanRepository.findAll();
    }

    public LoanApplication getApplicationById(Long id) {
        return loanRepository.findById(id).orElseThrow(() -> new RuntimeException("Loan Application not found"));
    }

    @Transactional
    public LoanApplication submitApplication(Long id) {
        LoanApplication app = getApplicationById(id);
        if (app.getState() != LoanState.DRAFT) {
            throw new IllegalStateException("Only DRAFT applications can be submitted");
        }
        app.setState(LoanState.SUBMITTED);
        return loanRepository.save(app);
    }

    @Transactional
    public LoanApplication approveApplication(Long id) {
        LoanApplication app = getApplicationById(id);
        if (app.getState() != LoanState.SUBMITTED) {
            throw new IllegalStateException("Only SUBMITTED applications can be approved");
        }
        app.setState(LoanState.APPROVED);
        return loanRepository.save(app);
    }

    @Transactional
    public LoanApplication rejectApplication(Long id) {
        LoanApplication app = getApplicationById(id);
        if (app.getState() != LoanState.SUBMITTED) {
            throw new IllegalStateException("Only SUBMITTED applications can be rejected");
        }
        app.setState(LoanState.REJECTED);
        return loanRepository.save(app);
    }

    @Transactional
    public List<Emi> generateEmiSchedule(Long id) {
        LoanApplication app = getApplicationById(id);
        if (app.getState() != LoanState.APPROVED) {
            throw new IllegalStateException("EMI schedule can only be generated for APPROVED loans");
        }

        // Check if schedule already generated
        List<Emi> existingEmis = emiRepository.findByLoanApplicationIdOrderByMonthNoAsc(id);
        if (!existingEmis.isEmpty()) {
            return existingEmis; // already generated
        }

        BigDecimal p = app.getAmount();
        BigDecimal r = app.getAnnualRate().divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        Integer n = app.getTenorMonths();

        // EMI = [P * R * (1+R)^N]/[(1+R)^N - 1]
        BigDecimal emiAmount;
        if (r.compareTo(BigDecimal.ZERO) == 0) {
            emiAmount = p.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal onePlusRPowerN = BigDecimal.ONE.add(r).pow(n);
            BigDecimal numerator = p.multiply(r).multiply(onePlusRPowerN);
            BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
            emiAmount = numerator.divide(denominator, 2, RoundingMode.HALF_UP);
        }

        BigDecimal balance = p;
        LocalDate currentDueDate = LocalDate.now().plusMonths(1);

        List<Emi> emiList = new ArrayList<>();
        for (int month = 1; month <= n; month++) {
            BigDecimal interestPart = balance.multiply(r).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPart = emiAmount.subtract(interestPart);

            // Last month adjustments
            if (month == n) {
                principalPart = balance;
                emiAmount = principalPart.add(interestPart);
                balance = BigDecimal.ZERO;
            } else {
                balance = balance.subtract(principalPart);
            }

            Emi emi = new Emi();
            emi.setLoanApplication(app);
            emi.setMonthNo(month);
            emi.setDueDate(currentDueDate);
            emi.setPrincipal(principalPart);
            emi.setInterest(interestPart);
            emi.setInstallment(emiAmount);
            emi.setBalance(balance);

            emiList.add(emi);
            currentDueDate = currentDueDate.plusMonths(1);
        }

        return emiRepository.saveAll(emiList);
    }

    public List<Emi> getEmiSchedule(Long loanId) {
        return emiRepository.findByLoanApplicationIdOrderByMonthNoAsc(loanId);
    }

    public List<LoanApplication> getApplicationsByCustomerId(String customerId) {
        return loanRepository.findByCustomerId(customerId);
    }

    public List<LoanApplication> getApplicationsByState(LoanState state) {
        return loanRepository.findByState(state);
    }

    public List<LoanApplication> getApplicationsByProduct(LoanProduct product) {
        return loanRepository.findByProduct(product);
    }

    @Transactional
    public LoanApplication updateApplication(Long id, LoanApplication updatedApp) {
        LoanApplication existingApp = getApplicationById(id);
        if (existingApp.getState() != LoanState.DRAFT) {
            throw new IllegalStateException("Only DRAFT applications can be updated");
        }
        existingApp.setCustomerId(updatedApp.getCustomerId());
        existingApp.setProduct(updatedApp.getProduct());
        existingApp.setAmount(updatedApp.getAmount());
        existingApp.setTenorMonths(updatedApp.getTenorMonths());
        existingApp.setAnnualRate(updatedApp.getAnnualRate());
        return loanRepository.save(existingApp);
    }

    @Transactional
    public void deleteApplication(Long id) {
        LoanApplication existingApp = getApplicationById(id);
        if (existingApp.getState() != LoanState.DRAFT) {
            throw new IllegalStateException("Only DRAFT applications can be deleted");
        }
        loanRepository.delete(existingApp);
    }
}
