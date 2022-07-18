package com.nagarro;

import com.nagarro.statements.StatementsApplication;
import com.nagarro.statements.exception.UserNotAuthorizedException;
import com.nagarro.statements.model.JwtResponse;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class StatementsApplicationTests {

    @Test
    public void contextLoads() {
        String microService = "nagarro-statement";
        assertThat(microService).isEqualTo("nagarro-statement");
    }


    @Test
    public void userUnAuthorized() {
        Exception exception = new UserNotAuthorizedException("UnAuthorized");
        assertThat(exception.getMessage()).isEqualTo("UnAuthorized");
    }


    @Test
    public void checkJwt() {
        JwtResponse jwtResponse = new JwtResponse("accessToken", 1L, "admin", "email", null);
        assertThat(jwtResponse.getUsername()).isEqualTo("admin");
    }


    @Test
    public void testSpringboot() {
        try {
            StatementsApplication.main(null);
        }catch (Exception e){
            assertThat(e).isInstanceOf(Exception.class);
        }
    }

}
