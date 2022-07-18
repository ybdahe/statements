package com.nagarro.statements.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.statements.dao.RoleDao;
import com.nagarro.statements.dao.UserDao;
import com.nagarro.statements.model.JwtResponse;
import com.nagarro.statements.util.JwtUtils;
import com.nagarro.statements.service.impl.UserDetailsImpl;
import com.nagarro.statements.service.impl.UserDetailsServiceImpl;
import com.nagarro.statements.service.StatementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatementController.class)
class StatementControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StatementService statementService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private JwtResponse jwtResponse;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    void getStatementAccessDenied() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("accountNumber", "0012250016001");
        mockMvc.perform(get("/api/v1/accounts/statements").params(paramsMap))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void getStatementForLast3Month() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("accountNumber", "0012250016001");
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn("admin");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new UserDetailsImpl(1L,"admin", null,"admin", new ArrayList<>()));
        mockMvc.perform(get("/api/v1/accounts/statements")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1ODA3MDEyNywiZXhwIjoxNjU4MDcwMTI3fQ.ATLCyw2nkR58_ZjEOjVxwNNo8LUdfWCJgz7Oa8k83Ta33lE4pTPrm9tLczTkKSUabKnm0sRgYJT9YUhSVhtawg")
                        .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").isNotEmpty())
                .andDo(print());
    }

    @Test
    void getStatementFor2020DateRange() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("accountNumber", "0012250016001");
        paramsMap.add("fromDate", "01.01.2020");
        paramsMap.add("toDate", "01.12.2020");
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn("admin");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new UserDetailsImpl(1L,"admin", null,"admin", new ArrayList<>()));
        mockMvc.perform(get("/api/v1/accounts/statements")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1ODA3MDEyNywiZXhwIjoxNjU4MDcwMTI3fQ.ATLCyw2nkR58_ZjEOjVxwNNo8LUdfWCJgz7Oa8k83Ta33lE4pTPrm9tLczTkKSUabKnm0sRgYJT9YUhSVhtawg")
                        .params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }

    @Test
    void getStatementForAmountRange() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("accountNumber", "0012250016001");
        paramsMap.add("fromAmount", "10");
        paramsMap.add("toAmount", "990");
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn("admin");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new UserDetailsImpl(1L,"admin", null,"admin", new ArrayList<>()));
        mockMvc.perform(get("/api/v1/accounts/statements")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1ODA3MDEyNywiZXhwIjoxNjU4MDcwMTI3fQ.ATLCyw2nkR58_ZjEOjVxwNNo8LUdfWCJgz7Oa8k83Ta33lE4pTPrm9tLczTkKSUabKnm0sRgYJT9YUhSVhtawg")
                        .params(paramsMap))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void getStatementInvalidParameter() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("accountNumber", "dsada");
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn("admin");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new UserDetailsImpl(1L,"admin", null,"admin", new ArrayList<>()));
        mockMvc.perform(get("/api/v1/accounts/statements")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1ODA3MDEyNywiZXhwIjoxNjU4MDcwMTI3fQ.ATLCyw2nkR58_ZjEOjVxwNNo8LUdfWCJgz7Oa8k83Ta33lE4pTPrm9tLczTkKSUabKnm0sRgYJT9YUhSVhtawg")
                        .params(paramsMap))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void getStatementInvalidParameterNotAllowedForTestUser() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("accountNumber", "001225001600");
        paramsMap.add("fromDate", "01.01.2020");
        paramsMap.add("toDate", "01.12.2020");
        when(jwtUtils.validateJwtToken(any())).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken(any())).thenReturn("user");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new UserDetailsImpl(2L,"user", null,"user", new ArrayList<>()));
        mockMvc.perform(get("/api/v1/accounts/statements")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY1ODA3MDEyNywiZXhwIjoxNjU4MDcwMTI3fQ.ATLCyw2nkR58_ZjEOjVxwNNo8LUdfWCJgz7Oa8k83Ta33lE4pTPrm9tLczTkKSUabKnm0sRgYJT9YUhSVhtawg")
                        .params(paramsMap))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
