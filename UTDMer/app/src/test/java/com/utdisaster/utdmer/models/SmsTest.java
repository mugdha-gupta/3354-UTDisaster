package com.utdisaster.utdmer.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Timestamp;

import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class SmsTest {
    @Test
    public void testCompareTo(){
        Sms oldSms = new Sms();
        oldSms.setTime(new Timestamp(0));

        Sms newSms = new Sms();
        newSms.setTime(new Timestamp(500));

        assertTrue(oldSms.compareTo(newSms) < 0);

    }
}
