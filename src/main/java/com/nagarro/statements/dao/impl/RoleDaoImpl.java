package com.nagarro.statements.dao.impl;

import com.nagarro.statements.dao.RoleDao;
import com.nagarro.statements.model.ERole;
import com.nagarro.statements.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class RoleDaoImpl implements RoleDao {
    @Autowired
    private Environment env;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Role findByName(ERole name) {
        logger.info("RoleDao call started ");
        try {
            Role role = new Role();
            role.setId(Integer.valueOf(env.getProperty("roles." + name + ".id")));
            role.setName(ERole.valueOf(env.getProperty("roles." + name + ".name")));
            logger.info("RoleDao call end ");
            return role;
        } catch (Exception e) {
            logger.error("Role Not found");
            throw new UsernameNotFoundException("Role Not found");
        }
    }
}
