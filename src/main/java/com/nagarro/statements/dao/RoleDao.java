package com.nagarro.statements.dao;

import com.nagarro.statements.model.ERole;
import com.nagarro.statements.model.Role;

public interface RoleDao {
  Role findByName(ERole name);
}
