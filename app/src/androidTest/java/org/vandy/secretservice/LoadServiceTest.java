package org.vandy.secretservice;

import android.content.Context;
import android.content.Intent;

import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ServiceTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.vandy.secretservice.service.LoadService;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoadServiceTest {

    //  create a ServiceTestRule instance in the test by using the @Rule annotation
    @Rule
    public final ServiceTestRule loadServiceRule = new ServiceTestRule();

    @Test
    public void testWithStartedService() throws TimeoutException {

        Context context = InstrumentationRegistry
                .getInstrumentation().getContext();

        Intent intent = new Intent(context, LoadService.class);

        loadServiceRule.startService(intent);

        LoadService loadService = new LoadService();
        assertTrue(loadService.isStarted());
    }
}
