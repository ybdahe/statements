package com.nagarro.statements.dao.impl;

import com.nagarro.statements.dao.StatementDao;
import com.nagarro.statements.exception.DatabaseException;
import com.nagarro.statements.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Component
public class StatementDaoImpl implements StatementDao {

    public static final String ACCOUNT_QUERY = "SELECT s.account_id,a.account_number,s.datefield,s.id,s.amount FROM statement s,account a " +
            "WHERE s.account_id = a.id and a.account_number = ? ";
    public static final String DATE_QUERY = "and TO_DATE(datefield,'DD.MM.YYYY') BETWEEN TO_DATE(?,'DD.MM.YYYY') AND TO_DATE(?,'DD.MM.YYYY')";
    public static final String AMOUNT_QUERY = "and amount BETWEEN ? AND ?";
    public static final String ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String DATEFIELD = "datefield";
    public static final String ID = "id";
    public static final String AMOUNT = "amount";
    public static final String TOTAL_RECORDS_FOUND = "Total records found {} ";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder encoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<Statement> getStatement(String accountNumber, String fromDate, String toDate
            , Double fromAmount, Double toAmount) {
        logger.info("*********************** Statement Dao Start ***************************");
        try {
            if (fromDate == null && toDate == null && fromAmount == null && toAmount == null) {
                LocalDate currentDate = LocalDate.now();
                toDate = currentDate.getDayOfMonth() + "." + currentDate.getMonthValue() + "." + currentDate.getYear();
                LocalDate past3MonthsDate = LocalDate.now().minusMonths(3).atStartOfDay(ZoneId.systemDefault()).toLocalDate();
                fromDate = past3MonthsDate.getDayOfMonth() + "." + past3MonthsDate.getMonthValue() + "." + past3MonthsDate.getYear();
                return getStatementByAccountNumberAndDate(accountNumber, fromDate, toDate);
            } else if (fromDate != null && toDate != null && fromAmount == null && toAmount == null) {
                return getStatementByAccountNumberAndDate(accountNumber, fromDate, toDate);
            } else if (fromDate == null && toDate == null && fromAmount != null && toAmount != null) {
                return getStatementByAccountNumberAndAmount(accountNumber, fromAmount, toAmount);
            } else {
                return getStatementByAccountNumberDateAndAmount(accountNumber, fromDate, toDate, fromAmount, toAmount);
            }
        } catch (Exception e) {
            logger.error("Database query execution exception");
            throw new DatabaseException("Database query execution exception");
        }
    }

    public List<Statement> getStatementByAccountNumberAndAmount(String accountNumber, Double fromAmount, Double toAmount) {
        logger.info("*********************** Statement By Account Number And Amount Only Start ***************************");
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(ACCOUNT_QUERY);
        sqlQuery.append(AMOUNT_QUERY);
        sqlQuery.append(";");

        List<Statement> statements = jdbcTemplate.query(sqlQuery.toString(), (resultSet, rowNum) ->
                        new Statement(resultSet.getInt(ACCOUNT_ID),
                                encoder.encode(resultSet.getString(ACCOUNT_NUMBER)),
                                resultSet.getString(DATEFIELD)
                                , resultSet.getInt(ID),
                                resultSet.getDouble(AMOUNT)),
                accountNumber, fromAmount, toAmount);
        logger.info(TOTAL_RECORDS_FOUND , statements.size());
        logger.info("*********************** Statement By Account Number And Amount Only End ***************************");
        return statements;
    }

    public List<Statement> getStatementByAccountNumberAndDate(String accountNumber, String fromDate, String toDate) {
        logger.info("*********************** Statement By Account Number and Date Only Start ***************************");
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(ACCOUNT_QUERY);
        sqlQuery.append(DATE_QUERY);
        sqlQuery.append(";");

        List<Statement> statements = jdbcTemplate.query(sqlQuery.toString(), (resultSet, rowNum) ->
                        new Statement(resultSet.getInt(ACCOUNT_ID),
                                encoder.encode(resultSet.getString(ACCOUNT_NUMBER)),
                                resultSet.getString(DATEFIELD)
                                , resultSet.getInt(ID),
                                resultSet.getDouble(AMOUNT)),
                accountNumber, fromDate, toDate);
        logger.info(TOTAL_RECORDS_FOUND , statements.size());
        logger.info("*********************** Statement By Account Number and Date Only  End ***************************");
        return statements;
    }

    public List<Statement> getStatementByAccountNumberDateAndAmount(String accountNumber, String fromDate, String toDate
            , Double fromAmount, Double toAmount) {
        logger.info("*********************** Statement By Account Number Date and Amount Start ***************************");
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(ACCOUNT_QUERY);
        sqlQuery.append(DATE_QUERY);
        sqlQuery.append(AMOUNT_QUERY);
        sqlQuery.append(";");

        List<Statement> statements = jdbcTemplate.query(sqlQuery.toString(), (resultSet, rowNum) ->
                        new Statement(resultSet.getInt(ACCOUNT_ID),
                                encoder.encode(resultSet.getString(ACCOUNT_NUMBER)),
                                resultSet.getString(DATEFIELD)
                                , resultSet.getInt(ID),
                                resultSet.getDouble(AMOUNT)),
                accountNumber, fromDate, toDate, fromAmount, toAmount);
        logger.info(TOTAL_RECORDS_FOUND , statements.size());
        logger.info("*********************** Statement By Account Number Date and Amount End ***************************");
        return statements;
    }
}
