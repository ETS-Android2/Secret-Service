package org.vandy.secretservice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MapActivityTest {
    private MapActivity mapActivity;
    @Before
    public void setUp (){
        mapActivity = new MapActivity();
    }

    @Test
    public void checkIfViewIntentIsProducedPasses() throws Exception {
        assertTrue(mapActivity.ifViewIntentIsSent());
    }
}
