package com.example.college.loan_application.controller;

import com.example.college.loan_application.model.Emi;
import com.example.college.loan_application.model.LoanApplication;
import com.example.college.loan_application.model.LoanProduct;
import com.example.college.loan_application.model.LoanState;
import com.example.college.loan_application.service.LoanApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanService;

    @PostMapping
    public ResponseEntity<LoanApplication> createApplication(@Valid @RequestBody LoanApplication application) {
        return ResponseEntity.ok(loanService.createApplication(application));
    }

    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApplications() {
        return ResponseEntity.ok(loanService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanApplication> getApplication(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getApplicationById(id));
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<LoanApplication> submitApplication(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.submitApplication(id));
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<LoanApplication> approveApplication(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.approveApplication(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<LoanApplication> rejectApplication(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.rejectApplication(id));
    }

    @PostMapping("/{id}/generate-emi")
    public ResponseEntity<List<Emi>> generateEmiSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.generateEmiSchedule(id));
    }

    @GetMapping("/{id}/emis")
    public ResponseEntity<List<Emi>> getEmiSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getEmiSchedule(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanApplication>> getApplicationsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(loanService.getApplicationsByCustomerId(customerId));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<LoanApplication>> getApplicationsByState(@PathVariable LoanState state) {
        return ResponseEntity.ok(loanService.getApplicationsByState(state));
    }

    @GetMapping("/product/{product}")
    public ResponseEntity<List<LoanApplication>> getApplicationsByProduct(@PathVariable LoanProduct product) {
        return ResponseEntity.ok(loanService.getApplicationsByProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanApplication> updateApplication(@PathVariable Long id, @Valid @RequestBody LoanApplication application) {
        return ResponseEntity.ok(loanService.updateApplication(id, application));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        loanService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
}
