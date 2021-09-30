package org.vandy.secretservice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginActivityTest {

    private LoginActivity loginActivity;
    @Before
    public void setUp (){
        loginActivity = new LoginActivity();
    }

    @Test
    public void checkNonZeroLengthOfNameAndPasswordPasses() throws Exception {
        loginActivity.name = "a";
        loginActivity.password = "1";
        assertTrue(loginActivity.checkNonZeroLengthOfNameAndPassword());
    }

    @Test
    public void checkNonZeroLengthOfNameAndPasswordFails() throws Exception {
        loginActivity.name = "";
        loginActivity.password = "";
        assertFalse(loginActivity.checkNonZeroLengthOfNameAndPassword());
    }

    @Test
    public void checkCorrectNameAndPasswordPasses () throws Exception {
        loginActivity.name = "Bill";
        loginActivity.password = "12345";
        loginActivity.dbName = "Bill";
        loginActivity.dbPassword = "12345";
        assertTrue(loginActivity.checkCorrectNameAndPassword());
    }

    @Test
    public void checkCorrectNameAndPasswordFails () throws Exception {
        loginActivity.name = "Bill";
        loginActivity.password = "12345";
        loginActivity.dbName = "Jim";
        loginActivity.dbPassword = "67890";
        assertFalse(loginActivity.checkCorrectNameAndPassword());
    }
}