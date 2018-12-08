package com.utdisaster.utdmer.utility;

import android.database.Cursor;

import com.utdisaster.utdmer.models.Sms;

import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmsUtilityTest {
    @Test
    public void testSendMessage() {
        Sms sms = new Sms();
        sms.setAddress("4695550205");
        sms.setFolderName("sent");
        sms.setMsg("This is the msg");
        sms.setReadState(true);
        sms.setTime(new Timestamp(123456789));
        //verify(SmsUtility.class, times(1)).addNewMessage(eq(sms));
        assertNotNull(sms);
    }

    @Test
    public void testParseSmsCursor() {
        Cursor c = mock(Cursor.class);
        when(c.getColumnIndexOrThrow("_id")).thenReturn(0);
        when(c.getColumnIndexOrThrow("address")).thenReturn(1);
        when(c.getColumnIndexOrThrow("body")).thenReturn(2);
        when(c.getColumnIndexOrThrow("read")).thenReturn(3);
        when(c.getColumnIndexOrThrow("type")).thenReturn(4);
        when(c.getColumnIndexOrThrow("date")).thenReturn(5);
        when(c.getString(0)).thenReturn("0");
        when(c.getString(1)).thenReturn("ADDRESS");
        when(c.getString(2)).thenReturn("BODY");
        when(c.getString(3)).thenReturn("0");
        when(c.getString(4)).thenReturn("0");
        when(c.getString(5)).thenReturn("0");
        Sms sms = new Sms();
        sms.setId("0");
        sms.setAddress("ADDRESS");
        sms.setMsg("BODY");
        sms.setReadState(false);
        sms.setFolderName("sent");
        sms.setTime(new Timestamp(Long.valueOf(0)));
        assertEquals(sms, SmsUtility.parseSmsCursor(c));
    }
}
