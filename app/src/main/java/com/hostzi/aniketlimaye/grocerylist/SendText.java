package com.hostzi.aniketlimaye.grocerylist;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Aniket on 01/09/2015.
 */
public class SendText extends Activity implements ContactConfirmationDialog.NoticeDialogListener,
        PhoneConfirmationDialog.PhoneDialogListener
{

    private static final String STATETAG = "SendText State Change";
    private static final String DEBUG = "Debug";
    private ArrayList<Tuple<String, String[]>> contacts = null;
    private String txtMsg = "", correctContact = "", correctPhone ="";
    private boolean wasConfirmed = false;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(STATETAG, "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(STATETAG, "onRestoreInstanceState");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(STATETAG, "onCreate");
        setContentView(R.layout.sendtext);

        generateTextMessage(getIntent().getExtras());
        if (contacts == null) {
            contacts = getContacts();
        }

        ((Button) findViewById(R.id.sendTextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchExp = ((EditText) findViewById(R.id.sendTextET)).getText().toString();
                //get mathing contacts from search expression
                ArrayList<String> matches = new ArrayList<String>();
                Iterator iterator = contacts.iterator();
                while (iterator.hasNext()) {
                    Tuple<String, String[]> current = (Tuple<String, String[]>) iterator.next();
                    if (current.first.toLowerCase().contains(searchExp.toLowerCase())) {
                        matches.add(current.first);
                    }
                }

                //if no mathing names are found, nothing else is done. The user has to hit
                //the go button again to start another search.
                if (matches.size() == 0) {
                    Toast.makeText(getBaseContext(), "Contact not found. Try parts of their name.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    ContactConfirmationDialog contactDialog = new ContactConfirmationDialog();
                    Bundle names = new Bundle();
                    names.putStringArray("matchedNames", matches.toArray(new String[matches.size()]));
                    contactDialog.setArguments(names);
                    contactDialog.show(getFragmentManager(), "contact_dialog_tag");
                    //after this a return value is either returned or the whole process is repeated.
                    //Return value is stored in 'correctContact'

                }


            }
        });


    }


    private void generateTextMessage(Bundle bundle)
    {
        String[] foodGroups = bundle.getStringArray("foodGroups");
        String[] currentArray = null, rowSplit = null;
        txtMsg += "Weekly grocery list: \n";

        for(int i = 0; i < bundle.size() - 1; i++)
        {
            currentArray = bundle.getStringArray(foodGroups[i].replaceAll("\\s", "")
                    .toLowerCase() + "_cache");
            for (int k = 0; k < currentArray.length; k++)
            {
                rowSplit = currentArray[k].split(",");
                if(Integer.parseInt(rowSplit[1])!= 0 && Boolean.parseBoolean(rowSplit[2]))
                {
                    //put manual spaces instad of \t
                    txtMsg += rowSplit[0]+"    "+Integer.parseInt(rowSplit[1])+"\n";
                }
            }
        }

    }

    private ArrayList<Tuple<String, String[]>> getContacts()
    {
        ArrayList<Tuple<String, String[]>> contacts = new ArrayList<Tuple<String, String[]>>();

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        StringBuffer output = new StringBuffer();

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                if (hasPhoneNumber > 0) {

                    ArrayList<String> phoneNums = new ArrayList<String>();
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        phoneNums.add(phoneNumber);
                    }

                    phoneCursor.close();

                    contacts.add(new Tuple<String, String[]>(name,
                            phoneNums.toArray(new String[phoneNums.size()])));

                    // Query and loop for every email of the contact
//                            Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
//
//                            while (emailCursor.moveToNext()) {
//
//                                email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
//
//                                output.append("\nEmail:" + email);
//
//                            }
//
//                            emailCursor.close();
                }
            }


        }

        return contacts;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {}

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {}



    @Override
    public void onContactConfirmReturn(String name)
    {
        correctContact = name;
        Iterator iterator = contacts.iterator();
        String[] phoneNums  = null;
        while(iterator.hasNext())
        {
            Tuple<String, String[]> current = (Tuple<String, String[]>)iterator.next();
            if(current.first.equals(name))
            {
                phoneNums = current.second;
                break;
            }
        }

        PhoneConfirmationDialog dialog = new PhoneConfirmationDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray("phoneNums", phoneNums);
        bundle.putString("textMessage", txtMsg);
        bundle.putString("contactName", name);
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "phone_dialog_tag");
    }

    @Override
    public void onPhoneConfirmReturn(String phoneNumber)
    {
        correctPhone = phoneNumber;

        String confirm = "Contact: " + correctContact + "\n" + "Phone number: " + correctPhone + "\n" + "Text message:\n" + txtMsg;
        wasConfirmed = false;

        SmsManager.getDefault().sendTextMessage(correctPhone, null, txtMsg, null, null);
        Toast.makeText(this,"Text successfully sent!", Toast.LENGTH_LONG).show();

//        //create an alert dialog to confirm what will be sent:
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());
//        alertDialogBuilder.setTitle("Confirm details");
//
//        // set dialog message
//        alertDialogBuilder
//                .setMessage(confirm)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        wasConfirmed = true;
//                        SmsManager.getDefault().sendTextMessage(correctPhone, null, txtMsg, null, null);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//
//                        // if this button is clicked, just close
//                        // the dialog box and do nothing
//                        dialog.dismiss();
//                    }
//                });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
    }

//    public void sendSMS(String phoneNumber, String message)
//    {
//        String SENT = "SMS_SENT";
//        String DELIVERED = "SMS_DELIVERED";
//
//        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
//                new Intent(SENT), 0);
//
//        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//                new Intent(DELIVERED), 0);
//
//        //---when the SMS has been sent---
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS sent",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(getBaseContext(), "Generic failure",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(getBaseContext(), "No service",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(getBaseContext(), "Null PDU",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(getBaseContext(), "Radio off",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(SENT));
//
//        //---when the SMS has been delivered---
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));
//
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
//    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(STATETAG, "onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(STATETAG, "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(STATETAG, "onResume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(STATETAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(STATETAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(STATETAG, "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();
        Log.i(STATETAG, "finish");
    }

    @Override
    public void recreate() {
        super.recreate();
        Log.i(STATETAG, "recreate");
    }

    @Override
    public boolean isFinishing() {
        Log.i(STATETAG, "isFinishing");
        return super.isFinishing();

    }

    @Override
    public boolean isDestroyed() {
        Log.i(STATETAG, "isDestroyed");
        return super.isDestroyed();
    }

}
