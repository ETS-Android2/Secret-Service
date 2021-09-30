package org.vandy.secretservice;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

    public class MainActivityTest {

        private MainActivity mainActivity;
        @Before
        public void setUp (){
            mainActivity = new MainActivity();
        }

        @Test
        public void checkIfLoginIntentIsProducedPasses() throws Exception {
            assertTrue(mainActivity.ifLoadServiceIntentIsSent());
        }
}
