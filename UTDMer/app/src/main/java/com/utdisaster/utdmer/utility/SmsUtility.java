package com.utdisaster.utdmer.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utdisaster.utdmer.models.Sms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SmsUtility {

    // Application context
    private static Context context;
    // ListView from mainActivity
    private static ListView messageView;

    public static void setContext(Context c) {
        context = c;
    }

    public static Context getContext() {
        return context;
    }

    public static void setMessageView(ListView listView) {
        messageView = listView;
    }

    public static ListView getMessageView() {
        return messageView;
    }

    public static boolean deleteSms(int id) {
        try {
            // Delete message with matching id
            context.getContentResolver().delete(
                    Uri.parse("content://sms/" + id), null, null);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static void updateMessageView() {
        // Get list of messages
        List<Sms> messageList = SmsUtility.getSmsInbox(context.getApplicationContext());
        // Find message view
        if(messageList!=null) {
            // Create adapter to display message
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, messageList);
            // Replace message view with messages
            messageView.setAdapter(arrayAdapter);
        }
    }

    public static void addNewMessage(Sms sms) {
        ContentValues values = new ContentValues();
        values.put("address", sms.getAddress());
        values.put("body", sms.getMsg());
        values.put("read", "0");
        values.put("date", sms.getTime().getTime());
        values.put("type", 1);
        Uri uri = Telephony.Sms.Inbox.CONTENT_URI;
        // Save message to content://sms
        context.getContentResolver().insert(uri, values);
    }

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
    private static List<Sms> getSmsInbox(Context context) {
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
