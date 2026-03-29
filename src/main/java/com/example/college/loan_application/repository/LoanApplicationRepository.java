package com.example.college.loan_application.repository;

import com.example.college.loan_application.model.LoanApplication;
import com.example.college.loan_application.model.LoanProduct;
import com.example.college.loan_application.model.LoanState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByCustomerId(String customerId);

    List<LoanApplication> findByState(LoanState state);

    List<LoanApplication> findByProduct(LoanProduct product);
}
