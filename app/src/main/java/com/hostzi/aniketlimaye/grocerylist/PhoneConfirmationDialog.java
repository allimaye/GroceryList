package com.hostzi.aniketlimaye.grocerylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Aniket on 01/09/2015.
 */
public class PhoneConfirmationDialog extends DialogFragment{

    boolean wasConfirmed = false;
    String phoneNumber = "";
    String textMessage = "";

    public interface PhoneDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onPhoneConfirmReturn(String phoneNumber);
    }

    // Use this instance of the interface to deliver action events
    PhoneDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PhoneDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement PhoneDialogListener");
        }
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        final String[] phoneNums = getArguments().getStringArray("phoneNums");
        final ArrayList selected = new ArrayList();


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Pick the correct phone number")
                .setSingleChoiceItems(phoneNums, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        selected.clear();
                        selected.add(whichButton);
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                mListener.onDialogPositiveClick(PhoneConfirmationDialog.this);
                PhoneDialogListener activity = (PhoneDialogListener) getActivity();
                phoneNumber = selected.size() == 0 ? phoneNums[0] : phoneNums[(int) selected.get(0)];
                activity.onPhoneConfirmReturn(phoneNumber);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mListener.onDialogNegativeClick(PhoneConfirmationDialog.this);
                dialog.dismiss();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

//    private void sendSMS(String phoneNumber, String message)
//    {
////        PendingIntent pi = PendingIntent.getActivity(getActivity(), 0,
////                new Intent(getActivity(),SendText.class), 0);
//
//        SmsManager.getDefault().sendTextMessage(phoneNumber, null, message, null, null);
//    }



}
