package com.nagarro.statements.controller;

import com.nagarro.statements.model.Statement;
import com.nagarro.statements.model.StatementResponse;
import com.nagarro.statements.service.StatementService;
import com.nagarro.statements.util.ValidatorUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "Manage accounts compliance", tags = {"Statements"})
@RequestMapping("/api/v1/accounts")
public class StatementController {

    @Autowired
    StatementService statementService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/statements")
    @ApiOperation(value = "API to get account statement", response = StatementResponse.class)
    public ResponseEntity<StatementResponse> getAccountStatement(
            @RequestParam() String accountNumber,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) Double fromAmount,
            @RequestParam(required = false) Double toAmount
    ) {
        logger.info("getStatement request received ");
        ValidatorUtils.validateRequest(accountNumber, fromDate, toDate, fromAmount, toAmount);
        List<Statement> statements = statementService.getStatement(accountNumber, fromDate, toDate, fromAmount, toAmount);
        logger.info("getStatement request Success ");
        return ResponseEntity.ok(new StatementResponse(statements.size(), statements));
    }
}
