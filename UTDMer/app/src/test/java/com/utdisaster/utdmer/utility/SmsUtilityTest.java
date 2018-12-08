package com.utdisaster.utdmer.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;

import com.utdisaster.utdmer.models.Sms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SmsManager.class, SmsUtility.class, Log.class, Telephony.Sms.Sent.class})
public class SmsUtilityTest {
    @Test
    public void testSendMessage() throws Exception {
        Context context = mock(Context.class);
        ContentResolver contentResolver = mock(ContentResolver.class);
        SmsManager smsManager = mock(SmsManager.class);
        SmsUtility.setContext(context);
        when(context.getContentResolver()).thenReturn(contentResolver);
        PowerMockito.mockStatic(SmsManager.class);
        BDDMockito.given(SmsManager.getDefault()).willReturn(smsManager);
        ContentValues values = PowerMockito.mock(ContentValues.class);
        doNothing().when(values).put(any(String.class), any(String.class));
        doNothing().when(values).put(any(String.class), any(Integer.class));
        PowerMockito.whenNew(ContentValues.class).withNoArguments().thenReturn(values);
        PowerMockito.mockStatic(Log.class);
        Uri uri = mock(Uri.class);
        Field field = PowerMockito.field(Telephony.Sms.Sent.class, "CONTENT_URI");
        field.set(Telephony.Sms.Sent.class, uri);
        Sms sms = new Sms();
        sms.setAddress("4695550205");
        sms.setFolderName("sent");
        sms.setMsg("This is the msg");
        sms.setReadState(true);
        sms.setTime(new Timestamp(123456789));


        SmsUtility.sendMessage(sms);
        // Verify sms is sent
        verify(smsManager, times(1)).sendTextMessage("4695550205", null,
                "This is the msg", null, null);
        // Verify sent sms is saved into Sms content
        verify(context, times(1)).getContentResolver();
        verify(contentResolver, times(1)).insert(eq(Telephony.Sms.Sent.CONTENT_URI), any(ContentValues.class));
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
        sms.setTime(new Timestamp(0));
        assertEquals(sms, SmsUtility.parseSmsCursor(c));
    }
}
