package org.vandy.secretservice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StartActivityTest {

    private StartActivity startActivity;
    @Before
    public void setUp (){
        startActivity = new StartActivity();
    }

    @Test
    public void checkIfLoginIntentIsProducedPasses() throws Exception {
        assertTrue(startActivity.ifLoginIntentIsSent());
    }
}
