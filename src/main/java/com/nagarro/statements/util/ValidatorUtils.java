package com.nagarro.statements.util;

import com.nagarro.statements.exception.InvalidParameterException;
import com.nagarro.statements.exception.UserNotAuthorizedException;
import com.nagarro.statements.model.ERole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtils {
    private ValidatorUtils() {}
    private static final String ACCOUNT_ERROR = "Invalid Parameter AccountNumber";
    private static final String FROM_DATE_ERROR = "Invalid Parameter fromDate";
    private static final String TO_DATE_ERROR = "Invalid Parameter toDate";

    public static void validateRequest(String accountNumber, String fromDate, String toDate, Double fromAmount, Double toAmount) {
        final Logger logger = LoggerFactory.getLogger(ValidatorUtils.class);
        logger.info("validating request for given parameters:-");
        // principal authentication from security context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(role -> role.equals(ERole.ROLE_USER.toString()))) {
            validateParams(fromDate, toDate, fromAmount, toAmount, logger);
        }

        validateAccountNumber(accountNumber, logger);
        if (fromAmount != null && fromAmount.isNaN()) {
            logger.error("Invalid Parameter fromAmount");
            throw new InvalidParameterException("Invalid Parameter fromAmount");
        }
        if (toAmount != null && toAmount.isNaN()) {
            logger.error("Invalid Parameter toAmount");
            throw new InvalidParameterException("Invalid Parameter toAmount");
        }
        validateDate(fromDate, logger, FROM_DATE_ERROR);
        validateDate(toDate, logger, TO_DATE_ERROR);
    }

    private static void validateDate(String toDate, Logger logger, String toDateError) {
        if (toDate != null) {
            if (toDate.isEmpty()) {
                logger.error(toDateError);
                throw new InvalidParameterException(toDateError);
            } else {
                String regex = "^(1[0-2]|0[1-9]).(3[01]|[12]\\d|0[1-9]).\\d{4}$";
                Pattern pattern = Pattern.compile(regex);
                Matcher m = pattern.matcher(toDate);
                if (!m.matches()) {
                    logger.error(toDateError);
                    throw new InvalidParameterException(toDateError);
                }
            }
        }
    }

    private static void validateAccountNumber(String accountNumber, Logger logger) {
        if (accountNumber.isEmpty()) {
            logger.error(ACCOUNT_ERROR);
            throw new InvalidParameterException(ACCOUNT_ERROR);
        } else {
            Pattern p = Pattern.compile("^\\d{13}$");
            Matcher m = p.matcher(accountNumber);
            if (!m.matches()) {
                logger.error(ACCOUNT_ERROR);
                throw new InvalidParameterException(ACCOUNT_ERROR);
            }
        }
    }

    private static void validateParams(String fromDate, String toDate, Double fromAmount, Double toAmount, Logger logger) {
        if (fromDate != null || toDate != null || fromAmount != null || toAmount != null) {
            logger.error("User not authorized to send parameters in request");
            throw new UserNotAuthorizedException("User not authorized to send parameters in request");
        }
    }
}
