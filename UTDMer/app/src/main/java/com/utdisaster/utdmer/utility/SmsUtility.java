package com.utdisaster.utdmer.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.utdisaster.utdmer.models.Sms;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class SmsUtility {
    // Application context
    private static Context context;
    // ListView from mainActivity
    private static ListView messageView;
    private static String address;
    // A hashmap of address to messages
    private static HashMap<String, ArrayList<Sms>> conversations;
    private static final String TAG = SmsUtility.class.getName();

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

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        SmsUtility.address = address;
    }

    // This retrieves the Sms object from the cursor
    public static Sms parseSmsCursor(Cursor c) {
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
    // Deletes a message from memory
    public static boolean deleteSmsFromMemory(int id) {
        try {
            // Delete message with matching id
            context.getContentResolver().delete(
                    Uri.parse("content://sms/" + id), null, null);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    // Adds a new message to memory
    public static void addNewMessageToMemory(Sms sms) {
        Uri uri = null;
        ContentValues values = new ContentValues();
        values.put("address", sms.getAddress());
        values.put("body", sms.getMsg());
        if(sms.getTime() != null) {
            values.put("date", sms.getTime().getTime());
        } else {
            values.put("date", System.currentTimeMillis());
        }
        if(sms.isReadState()) {
            values.put("read", "1");
        } else {
            values.put("read", "0");
        }
        if("inbox".equals(sms.getFolderName())) {
            values.put("type", 1);
            uri = Telephony.Sms.Inbox.CONTENT_URI;
        } else if("sent".equals(sms.getFolderName())) {
            values.put("type", 0);
            uri = Telephony.Sms.Sent.CONTENT_URI;
        }
        // Save message to content://sms
        if(uri != null) {
            Log.v(TAG, "Saving message to " + sms.getFolderName());
            context.getContentResolver().insert(uri, values);
        } else {
            Log.e(TAG, "Error saving message: " + sms);
        }
    }
    // Retrieves all messsages in memory
    public static List<Sms> getAllMessagesInMemory(Context context){
        ContentResolver contentResolver = context.getContentResolver();
        // Request sms messages
        Cursor smsCursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
        List<Sms> messages = new ArrayList<>();

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
        return messages;
    }
    // Sends a message using an sms manager
    public static void sendMessage(Sms sms) {
        SmsManager smsManager = SmsManager.getDefault();
        // Send message
        smsManager.sendTextMessage(sms.getAddress(), null, sms.getMsg(), null, null);
        // Save message
        addNewMessageToMemory(sms);
    }
    // Updates the inbox list view
    public static void updateInboxMessageView() {

        List<Sms> messageList = SmsUtility.getInboxMessages(context.getApplicationContext());
        // Find message view
        if (messageList != null) {
            // Create adapter to display message
            ArrayAdapter arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, messageList);
            // Replace message view with messages
            messageView.setAdapter(arrayAdapter);
        }

    }
    // Updates the conversation view using an ListView parameter
    public static void updateConversationMessageView(ListView cView){
            List<Sms> conversationMessageList = SmsUtility.getMessagesInConversationByAddress(address);
            // Update view if messages exist
            if(conversationMessageList!=null) {
                ArrayAdapter arrayAdapter = new ArrayAdapter<>(context.getApplicationContext(), android.R.layout.simple_list_item_1, conversationMessageList);
                cView.setAdapter(arrayAdapter);
            }

    }
    // Get SMS messages in the inbox
    public static List<Sms> getInboxMessages(Context context) {
        List<Sms> messages = getAllMessagesInMemory(context);

        // Sort messages by timestamp
        Collections.sort(messages);
        // Most recent message first
        Collections.reverse(messages);
        // Sort the messages and populate conversations hashmap
        sortMessagesIntoConversations(messages);

        return getRecentMessagesFromConversations();

    }
    // Gets the most recent message from every conversation for inbox
    private static List<Sms> getRecentMessagesFromConversations() {
        ArrayList<Sms> recentMessages = new ArrayList<>();
        // For every conversation add one message
        for(String address: conversations.keySet()){
            recentMessages.add(conversations.get(address).get(0));
        }

        // Sort and order
        Collections.sort(recentMessages);
        Collections.reverse(recentMessages);

        return recentMessages;
    }
    // Sorts messages by conversation
    private static void sortMessagesIntoConversations(List<Sms> messages){
        conversations = new HashMap<>();

        // For every given message
        for(Sms message: messages){
            ArrayList<Sms> prevMessages;

            // If the conversation already exists, retrieve the conversation messages
            if(conversations.containsKey(message.getAddress())){
                prevMessages = conversations.get(message.getAddress());
            }

            else{
                prevMessages = new ArrayList<Sms>();
            }

            prevMessages.add(message);
            conversations.put(message.getAddress(), prevMessages);
        }
    }
    // Returns the messages related to given address
    public static List<Sms> getMessagesInConversationByAddress(String address){
        List<Sms> convoMess = conversations.get(address);
        // Reverses because most recent should be at the bottom
        Collections.reverse(convoMess);
        return convoMess;
    }
}
