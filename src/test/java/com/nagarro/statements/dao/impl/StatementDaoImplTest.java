package com.nagarro.statements.dao.impl;

import com.nagarro.statements.exception.DatabaseException;
import com.nagarro.statements.model.Accounts;
import com.nagarro.statements.model.Statement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementDaoImplTest {

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    StatementDaoImpl statementDao;

    @Test
    void findByUserName() {
        String accountNumber = "0012250016001";
        List<Statement> list = new ArrayList<>();
        Statement statement = new Statement(1, "0012250016001", "01.01.2022", 2, 10.12);
        list.add(statement);
        assertThat(statementDao.getStatement(accountNumber, null, null, null, null)).isEmpty();
        assertThat(statementDao.getStatementByAccountNumberAndDate(accountNumber, "01.01.2022", "01.01.2022")).isEmpty();
        assertThat(statementDao.getStatementByAccountNumberAndAmount(accountNumber,  1000.00, 20000.00)).isEmpty();
        assertThat(statementDao.getStatementByAccountNumberDateAndAmount(accountNumber, null, null, null, null)).isEmpty();

    }

    @Test
    void findByUserNameException() {
        String accountNumber = "0012250016001";
        List<Statement> list = new ArrayList<>();
        Accounts accounts = new Accounts(1,"Saving","0012250016001");
        Statement statement = new Statement(accounts.getId(), accounts.getAccountNumber(), "01.01.2020", 2, 10.12);
        list.add(statement);
        when(jdbcTemplate.query(anyString(), (ResultSetExtractor<Object>) any())).thenReturn(new Exception());
        try {
            statementDao.getStatement(accountNumber, null, null, null, null);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(DatabaseException.class);
        }
    }
}