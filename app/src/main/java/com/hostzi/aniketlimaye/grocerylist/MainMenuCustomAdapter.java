package com.hostzi.aniketlimaye.grocerylist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

/**
 * Created by Aniket on 31/08/2015.
 */
public class MainMenuCustomAdapter extends ArrayAdapter<String> {


    public MainMenuCustomAdapter(Context context, String[] foodGroups) {
        super(context, R.layout.main_custom_row, foodGroups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.main_custom_row, parent, false);

        final Button foodGroupButton = (Button)customView.findViewById(R.id.FoodGroupButton);

        foodGroupButton.setText(getItem(position));

        switch(getItem(position).replaceAll("\\s", "").toLowerCase())
        {
            case "dairy":
                foodGroupButton.setBackgroundColor(Color.parseColor("#ffff00"));
                foodGroupButton.setTextColor(Color.parseColor("#000000"));
                break;
            case "fruits&vegetables":
                foodGroupButton.setBackgroundColor(Color.parseColor("#00FF00"));
                foodGroupButton.setTextColor(Color.parseColor("#000000"));
                break;
            case "grain":
                foodGroupButton.setBackgroundColor(Color.parseColor("#ffd700"));
                foodGroupButton.setTextColor(Color.parseColor("#000000"));
                break;
            case "meat":
                foodGroupButton.setBackgroundColor(Color.parseColor("#CC0000"));
                break;
            case "other":
                //foodGroupButton.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.DARKEN);
                foodGroupButton.setBackgroundColor(Color.parseColor("#0099FF"));
                break;
        }

        foodGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), FoodList.class);
                String buttonText = foodGroupButton.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("foodGroup", buttonText.replaceAll("\\s", "").toLowerCase());
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });

        return customView;



    }
}
