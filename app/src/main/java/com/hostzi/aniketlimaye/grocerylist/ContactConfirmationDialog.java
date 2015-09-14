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
public class ContactConfirmationDialog extends DialogFragment {

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onContactConfirmReturn(String name);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction

        final String[] contactOptions = getArguments().getStringArray("matchedNames");
        final ArrayList selected = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Pick the correct contact")
        .setSingleChoiceItems(contactOptions, 0, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            selected.clear();
            selected.add(whichButton);
        }
    });
      builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            mListener.onDialogPositiveClick(ContactConfirmationDialog.this);
            NoticeDialogListener activity = (NoticeDialogListener) getActivity();
            if(selected.size() == 0)
            {
                activity.onContactConfirmReturn(contactOptions[0]);
            }
            else
            {
                activity.onContactConfirmReturn(contactOptions[(int) selected.get(0)]);
            }

        }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            mListener.onDialogNegativeClick(ContactConfirmationDialog.this);
        }
    });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
