package com.utdisaster.utdmer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.utdisaster.utdmer.models.Sms;
import com.utdisaster.utdmer.utility.SmsUtility;

import java.sql.Timestamp;

// Implements broadcast receiver to actually send messages
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    // When a message is received, this function is called
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Create Sms obj
                Sms sms = new Sms();
                sms.setAddress(msgs[i].getOriginatingAddress());
                sms.setFolderName("inbox");
                sms.setMsg(msgs[i].getMessageBody());
                sms.setReadState(false);
                sms.setTime(new Timestamp(System.currentTimeMillis()));
                // Save message to device
                SmsUtility.addNewMessageToMemory(sms);
                // Update message list
                SmsUtility.updateInboxMessageView();
            }
        }
    }
}
