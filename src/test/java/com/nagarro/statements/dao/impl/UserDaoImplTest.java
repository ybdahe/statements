package com.nagarro.statements.dao.impl;

import com.nagarro.statements.model.ERole;
import com.nagarro.statements.model.Role;
import com.nagarro.statements.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {

    @Mock
    Environment environment;

    @InjectMocks
    UserDaoImpl userDao;

    @Mock
    PasswordEncoder encoder;

    @Mock
    RoleDaoImpl roleDao;

    @Test
    void findByUserName() {
        User user = new User();
        Set<Role> roles = new HashSet<>();
        user.setId(1L);
        user.setUsername("admin");
        user.setEmail("admin@gmail.com");
        user.setPassword("pwd");
        Role role = new Role();
        role.setId(1);
        role.setName(ERole.valueOf("ROLE_ADMIN"));
        roles.add(role);
        user.setRoles(roles);
        when(environment.getProperty("users." + user.getUsername() + ".id")).thenReturn(String.valueOf(1));
        when(environment.getProperty("users." + user.getUsername() + ".username")).thenReturn("admin");
        when(environment.getProperty("users." + user.getUsername() + ".email")).thenReturn("admin@gmail.com");
        when(environment.getProperty("users." + user.getUsername() + ".password")).thenReturn("admin");
        when(environment.getProperty("users." + user.getUsername() + ".roles")).thenReturn("ROLE_ADMIN");
        when(roleDao.findByName(ERole.ROLE_ADMIN)).thenReturn(role);
        when(encoder.encode("admin")).thenReturn(anyString());
        assertThat(userDao.findByUsername(user.getUsername()).get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void findByUserNameException() {
        User user = new User();
        user.setId(1L);
        user.setUsername("admin");
        when(environment.getProperty("users." + "XYX" + ".name")).thenReturn(String.valueOf(new Exception()));
        try {
            userDao.findByUsername(user.getUsername());
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UsernameNotFoundException.class);
        }
    }
}