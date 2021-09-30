package org.vandy.secretservice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ViewActivityTest {
    private ViewActivity viewActivity;
    @Before
    public void setUp (){
        viewActivity = new ViewActivity();
    }

    @Test
    public void checkIfMapIntentIsProducedPasses() throws Exception {
        assertTrue(viewActivity.ifMapIntentIsSent());
    }
}
