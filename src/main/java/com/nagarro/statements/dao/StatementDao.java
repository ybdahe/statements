package com.nagarro.statements.dao;

import com.nagarro.statements.model.Statement;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Component
public interface StatementDao {
    List<Statement> getStatement(String accountNumber, String fromDate, String toDate, Double fromAmount, Double toAmount);
}
