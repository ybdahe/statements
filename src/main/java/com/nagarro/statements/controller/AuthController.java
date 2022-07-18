package com.nagarro.statements.controller;

import com.nagarro.statements.dao.RoleDao;
import com.nagarro.statements.dao.UserDao;
import com.nagarro.statements.exception.UserDuplicateLoginException;
import com.nagarro.statements.exception.UserNotAuthorizedException;
import com.nagarro.statements.model.JwtResponse;
import com.nagarro.statements.model.LoginRequest;
import com.nagarro.statements.model.UserLogin;
import com.nagarro.statements.util.JwtUtils;
import com.nagarro.statements.service.impl.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDao userDao;

    @Autowired
    RoleDao roleDao;

    @Autowired
    JwtUtils jwtUtils;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, UserLogin> userLoginMap = new HashMap<>();

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("login request received for user {}  " ,loginRequest.getUsername());
        if (userLoginMap.get(loginRequest.getUsername()) != null && jwtUtils.validateJwtToken(userLoginMap.get(loginRequest.getUsername()).getToken())) {
                throw new UserDuplicateLoginException("User Not allowed to login twice Please logout using /api/auth/logout");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        if(authentication!=null){
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            logger.info("login request success for user {} " , loginRequest.getUsername());
            userLoginMap.put(loginRequest.getUsername(), new UserLogin(loginRequest.getUsername(), new Date(), jwt));
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        }
        else {
            throw new UserNotAuthorizedException("User not authorized");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<JwtResponse> logOutUser(@RequestParam() String userName) {
        logger.info("logout request received for user {} " , userName);
        String jwt = "";
        if (userLoginMap.get(userName) != null && jwtUtils.validateJwtToken(userLoginMap.get(userName).getToken())) {
                jwt = jwtUtils.invalidateJwtToken(userName);
        }
        userLoginMap.remove(userName);
        if(jwt.isEmpty()){
            throw new UserDuplicateLoginException("User not logged in or already logged out");
        }
        return ResponseEntity.ok(new JwtResponse(jwt,
                null,
                userName,
                null,
                null));
    }
}
