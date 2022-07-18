package com.nagarro.statements.dao;

import com.nagarro.statements.model.User;

import java.util.Optional;

public interface UserDao {
  Optional<User> findByUsername(String username);
}
