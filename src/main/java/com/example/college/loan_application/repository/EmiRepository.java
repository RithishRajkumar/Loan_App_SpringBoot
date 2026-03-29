package com.example.college.loan_application.repository;

import com.example.college.loan_application.model.Emi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmiRepository extends JpaRepository<Emi, Long> {
    List<Emi> findByLoanApplicationIdOrderByMonthNoAsc(Long loanId);
}
