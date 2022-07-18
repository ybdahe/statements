package com.nagarro.statements.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public
class Statement {
    private int accountId;
    private String accountNumber;
    private String datefield;
    private int id;
    private double amount;
}
