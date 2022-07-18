package com.nagarro.statements.service.impl;

import com.nagarro.statements.dao.StatementDao;
import com.nagarro.statements.model.Statement;
import com.nagarro.statements.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StatementServiceImpl implements StatementService {

    @Autowired
    StatementDao statementDao;

    @Override
    public List<Statement> getStatement(String accountNumber, String fromDate, String toDate, Double fromAmount, Double toAmount) {
        return statementDao.getStatement(accountNumber, fromDate, toDate, fromAmount, toAmount);
    }
}
