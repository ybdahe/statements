package com.nagarro.statements.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.statements.dao.RoleDao;
import com.nagarro.statements.dao.UserDao;
import com.nagarro.statements.model.JwtResponse;
import com.nagarro.statements.model.LoginRequest;
import com.nagarro.statements.util.JwtUtils;
import com.nagarro.statements.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    @Test
    void loginUserAccessDenied() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void loginUserTwiceError() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void loginUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "admin");
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void logOutUser() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("userName", "admin");
        mockMvc.perform(get("/api/auth/logout").params(paramsMap))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void logOutUserAlreadyLoggedOut() throws Exception {
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("userName", "admin");
        mockMvc.perform(get("/api/auth/logout").params(paramsMap))
                .andExpect(jsonPath("$.message").value("User not logged in or already logged out"))
                .andDo(print());
    }
}
