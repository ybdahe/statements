package com.nagarro.statements.service.impl;

import com.nagarro.statements.dao.impl.StatementDaoImpl;
import com.nagarro.statements.exception.DatabaseException;
import com.nagarro.statements.model.Statement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementServiceImplTest {

    @Mock
    StatementDaoImpl statementDao;

    @InjectMocks
    StatementServiceImpl statementService;

    @Test
    void getValidStatements() {
        String accountNumber = "0012250016001";
        List<Statement> list = new ArrayList<>();
        Statement statement = new Statement(1, "0012250016001", "01.01.2020", 2, 10.12);
        list.add(statement);
        when(statementDao.getStatement(accountNumber, null, null, null, null)).thenReturn(list);
        List<Statement> statementList = statementService.getStatement(accountNumber, null, null, null, null);
        assertThat(statementList).hasSameClassAs(list);
    }

    @Test
    void getEmptyListWhenNoDataFound() {
        String accountNumber = "0012250";
        List<Statement> list = new ArrayList<>();
        when(statementDao.getStatement(accountNumber, null, null, null, null)).thenReturn(list);
        List<Statement> statementList = statementService.getStatement(accountNumber, null, null, null, null);
        assertThat(statementList).isEmpty();
    }

    @Test
    void getErrorWhenInvalidParameter() {
        String accountNumber = "001225001601";
        String fromDate = "01012022";
        Exception exception = new DatabaseException("Database query execution exception");
        try {
            statementService.getStatement(accountNumber, fromDate, null, null, null);
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo(exception.getMessage());
        }
    }

}