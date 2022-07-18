package com.nagarro.statements.dao.impl;

import com.nagarro.statements.model.ERole;
import com.nagarro.statements.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleDaoImplTest {

    @Mock
    Environment environment;

    @InjectMocks
    RoleDaoImpl roleDao;

    @Test
    void findByName() {
        Role role = new Role();
        role.setId(Integer.valueOf(1));
        role.setName(ERole.valueOf("ROLE_ADMIN"));
        when(environment.getProperty("roles." + role.getName() + ".id")).thenReturn(String.valueOf(1));
        when(environment.getProperty("roles." + role.getName() + ".name")).thenReturn(ERole.ROLE_ADMIN.name());
        assertThat(roleDao.findByName(ERole.ROLE_ADMIN).getName()).isEqualTo(role.getName());
    }

    @Test
    void findByNameException() {
        Role role = new Role();
        role.setId(Integer.valueOf(1));
        role.setName(ERole.valueOf("ROLE_ADMIN"));
        when(environment.getProperty("roles." + role.getName() + ".name")).thenReturn(String.valueOf(new Exception()));
        try {
            roleDao.findByName(ERole.ROLE_ADMIN);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(UsernameNotFoundException.class);
        }
    }
}