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
class Accounts {
    private int id;
    private String accountType;
    private String accountNumber;
}
