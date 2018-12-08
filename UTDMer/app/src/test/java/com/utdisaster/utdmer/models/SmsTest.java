package com.utdisaster.utdmer.models;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Timestamp;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    @Test
    public void testEquals(){
        Sms sms1 = new Sms();
        sms1.setId("0");
        sms1.setTime(new Timestamp(1));
        sms1.setAddress("2");
        sms1.setFolderName("3");
        sms1.setMsg("4");
        sms1.setReadState(false);

        // Exactly same as Sms 1
        Sms sms2 = new Sms();
        sms2.setId("0");
        sms2.setTime(new Timestamp(1));
        sms2.setAddress("2");
        sms2.setFolderName("3");
        sms2.setMsg("4");
        sms2.setReadState(false);

        assertTrue(sms1.equals(sms2));

        // Id is different
        Sms sms3 = new Sms();
        sms2.setId("1");
        sms2.setTime(new Timestamp(1));
        sms2.setAddress("2");
        sms2.setFolderName("3");
        sms2.setMsg("4");
        sms2.setReadState(false);

        assertFalse(sms1.equals(sms3));

    }

    @Test
    public void testToString(){
        Sms sms1 = new Sms();
        String id, address, folder, msg;
        boolean readState;
        Timestamp time;

        id = "0";
        address = "1";
        msg = "2";
        time = new Timestamp(500);
        folder = "inbox";

        sms1.setId(id);
        sms1.setAddress(address);
        sms1.setMsg(msg);
        sms1.setTime(time);
        sms1.setFolderName(folder);

        String expected = "From: " + address + "\tId: " + id + "\nMessage: " + msg + "\nTimestamp: " + time;
        assertEquals(sms1.toString(), expected);

        folder = "sent";
        sms1.setFolderName(folder);

        expected = "To: " + address + "\tId: " + id + "\nMessage: " + msg + "\nTimestamp: " + time;
        assertEquals(sms1.toString(), expected);


        readState = false;
        folder = "other";
        sms1.setReadState(false);
        sms1.setFolderName("other");


        expected = "Sms{" + "id='" + id + '\'' + ", address='" + address + '\'' + ", msg='" + msg + '\'' +", readState=" + readState +
                ", time=" + time + ", folderName='" + folder + '\'' + '}';
        assertEquals(sms1.toString(), expected);
    }

    @Test
    public void testHashCode(){
        Sms sms1 = new Sms();
        sms1.setId("0");
        sms1.setTime(new Timestamp(1));
        sms1.setAddress("2");
        sms1.setFolderName("3");
        sms1.setMsg("4");
        sms1.setReadState(false);

        Timestamp time = new Timestamp(1);
        assertEquals(Objects.hash("0", "2", "4", false, time, "3"), sms1.hashCode());

    }
}
