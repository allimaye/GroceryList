package com.hostzi.aniketlimaye.grocerylist;

/**
 * Created by Aniket on 29/08/2015.
 */

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;


public class GroceryItemCustomAdapter extends ArrayAdapter<GroceryItem>
{
    public GroceryItemCustomAdapter(Context context, GroceryItem[] grocItems) {
        super(context, R.layout.food_custom_row, grocItems);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.food_custom_row, parent, false);

        TextView nameInput = (TextView)customView.findViewById(R.id.ItemName);
        final EditText qtyInput = (EditText)customView.findViewById(R.id.Qty);
        CheckBox checkInput = (CheckBox)customView.findViewById(R.id.CheckBox);

        final GroceryItem currentItem = getItem(position);
        nameInput.setText(currentItem.itemName);
        qtyInput.setText(currentItem.qty != 0 ? currentItem.qty+"" : "0");
        checkInput.setChecked(currentItem.isSelected);

        qtyInput.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                currentItem.qty = s.toString().equals("") ? 0 : Integer.parseInt(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        qtyInput.clearFocus();

        checkInput.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getItem(position).isSelected = isChecked;
                //set the date object of the grocery item to be the time at which it was last checked
                getItem(position).lastAccessed = new Date();

                String qtyText = qtyInput.getText().toString();
                if ((qtyText.equals("0") || qtyText.equals("")) && isChecked)
                {
                    qtyInput.setText("1");
                    getItem(position).qty = 1;
                }
            }
        });


        return customView;

    }
}
