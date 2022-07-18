package com.nagarro.statements.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public
class StatementResponse {
    private int size;
    private List<Statement> statements;
}
