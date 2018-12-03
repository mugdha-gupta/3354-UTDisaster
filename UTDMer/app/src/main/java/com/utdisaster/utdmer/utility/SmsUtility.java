package com.utdisaster.utdmer.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.utdisaster.utdmer.models.Sms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmsUtility {

    private static Sms parseSmsCursor(Cursor c) {
        Sms sms = new Sms();
        sms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
        sms.setAddress(c.getString(c
                .getColumnIndexOrThrow("address")));
        sms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
        sms.setReadState(Boolean.valueOf(c.getString(c.getColumnIndex("read"))));
        sms.setTime(new Timestamp(Long.valueOf(c.getString(c.getColumnIndexOrThrow("date")))));
        // Sort sent messages vs received messages
        if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
            sms.setFolderName("inbox");
        } else {
            sms.setFolderName("sent");
        }

        return sms;
    }

    // Get SMS messages
    public static List<Sms> getSmsInbox(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        // Request sms messages
        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
        ArrayList<Sms> messages = new ArrayList<>();

        // process received sms
        if(smsCursor != null) {
            // verify cursor is valid and in good state
            int indexBody = smsCursor.getColumnIndex("body");
            if (indexBody < 0 || !smsCursor.moveToFirst()) {
                return null;
            }
            do {
                // Parse cursor data to build sms obj
                Sms sms = parseSmsCursor(smsCursor);
                messages.add(sms);
            } while (smsCursor.moveToNext());
            smsCursor.close();
        }
        if(messages.isEmpty()){
            return null;
        }
        // Sort messages by timestamp
        Collections.sort(messages);
        // Reverse list to display most recent message on top
        Collections.reverse(messages);
        return messages;
    }

}
