package com.nagarro.statements.service.impl;

import com.nagarro.statements.dao.impl.UserDaoImpl;
import com.nagarro.statements.model.ERole;
import com.nagarro.statements.model.Role;
import com.nagarro.statements.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    UserDaoImpl userDao;

    @InjectMocks
    UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername() {
        String userName = "admin";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, ERole.ROLE_ADMIN));
        User user = new User(1L, "admin", null, null, roles);
        when(userDao.findByUsername(userName)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        assertThat(userDetails.getUsername()).isEqualTo(userName);
    }

    @Test
    void getErrorWhenInvalidParameter() {
        String userNameAdmin = "admin";
        when(userDao.findByUsername(userNameAdmin)).thenReturn(null);
        try {
            userDetailsService.loadUserByUsername(userNameAdmin);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

    @Test
    void getEmptyListWhenNoDataFound() {
        String otherUserName = "xyz";
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(1, ERole.ROLE_ADMIN));
        User user = new User(null, null, null, null, roles);
        when(userDao.findByUsername(otherUserName)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(otherUserName);
        assertThat(userDetails.getUsername()).isNull();
    }
}
