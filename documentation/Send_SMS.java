// *** sources: https://www.tutorialspoint.com/android/android_sending_sms.htm
// https://www.youtube.com/watch?v=asIPXTM6xms




package com.utdisaster.utdmer;



import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;


import android.app.Activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.ContextCompat;
import android.telephony.SmsManager;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.util.Log;

import android.widget.Button;
import android.widget.EditText;


import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;


import android.widget.Toast;





public class MainActivity_3 extends Activity {
   private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0 ; // variable is an int-constant defined by the app

   String phoneNum;
   String msg;

   Button buttonToSend;

   EditText text_Msg;
   EditText text_PhoneNum;


   @Override
   protected void onCreate(Bundle savedInstanceState) { // onCreate bundle is where the activity is initialized 
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      buttonToSend = (Button) findViewById(R.id.btnSendSMS);
      text_PhoneNum = (EditText) findViewById(R.id.editText);
      text_Msg = (EditText) findViewById(R.id.editText2);

      buttonToSend.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            sendSMSMessage();

// try/catch alternative:

            /*
            try {
               SmsManager smsMngr = SmsManager.getDefault();
               smsMngr.sendTextMessage(phoneNum, null, msg, null, null);
               Toast.makeText(getApplicationContext(), "SMS has successfully been sent.", Toast.LENGTH_LONG).show();
            }

            catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS failed to send!", Toast.LENGTH_LONG).show();
            }

            */

         }
      });
   }
	


   protected void sendSMSMessage() {
      phoneNum = text_PhoneNum.getText().toString();       // retreive message
      message = text_Msg.getText().toString();
		
      if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
            } else {
               ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                  MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
      }
   }

   // switch case to determine the source of actvity, i.e which activity one is heading out of 

   // upon sending an sms, the 'Toast' feature is used for app to display notifiations 
   // flag-based variable 'LENGTH_LONG' is the value that represents the duration in which the toast notification will be displayed for:
   // 3 seconds in this case 
	
   @Override
   public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
      switch (requestCode) {
         // sending an SMS by utilizing an Intent, an operation's abstract attributes
         case MY_PERMISSIONS_REQUEST_SEND_SMS: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                  SmsManager smsMngr = SmsManager.getDefault();
                  smsMngr.sendTextMessage(phoneNum, null, msg, null, null);
                  Toast.makeText(getApplicationContext(), "SMS has successfully been sent.", 
                     Toast.LENGTH_LONG).show();
            } else {
               Toast.makeText(getApplicationContext(), 
                  "SMS failed to send!", Toast.LENGTH_LONG).show();
               return;
            }
         }
      }

   }
}