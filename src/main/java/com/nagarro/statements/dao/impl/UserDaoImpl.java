package com.nagarro.statements.dao.impl;

import com.nagarro.statements.dao.RoleDao;
import com.nagarro.statements.dao.UserDao;
import com.nagarro.statements.model.ERole;
import com.nagarro.statements.model.Role;
import com.nagarro.statements.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class UserDaoImpl implements UserDao {
    @Autowired
    private Environment env;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    PasswordEncoder encoder;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String USERS = "users.";

    @Override
    public Optional<User> findByUsername(String username) {
        logger.info("UserDao call started ");
        try {
            User user = new User();
            Set<Role> roles = new HashSet<>();
            user.setId(Long.valueOf(env.getProperty(USERS + username + ".id")));
            user.setUsername(env.getProperty(USERS + username + ".username"));
            user.setEmail(env.getProperty(USERS + username + ".email"));
            user.setPassword(encoder.encode(env.getProperty(USERS + username + ".password")));
            roles.add(roleDao.findByName(ERole.valueOf(env.getProperty(USERS + username + ".roles"))));
            user.setRoles(roles);
            logger.info("UserDao call end ");
            return Optional.of(user);
        } catch (Exception e) {
            logger.error("User Not found");
            throw new UsernameNotFoundException("User Not found");
        }
    }
}
