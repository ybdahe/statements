package com.nagarro.statements.service;

import com.nagarro.statements.model.Statement;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public interface StatementService {
    List<Statement> getStatement(String accountNumber, String fromDate, String toDate, Double fromAmount, Double toAmount);
}
