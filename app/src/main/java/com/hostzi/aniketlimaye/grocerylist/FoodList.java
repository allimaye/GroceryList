package com.hostzi.aniketlimaye.grocerylist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by Aniket on 31/08/2015.
 */
public class FoodList extends Activity {

    private String groupName = null;
    private GroceryItem[] grocItems = null;
    private ArrayAdapter<GroceryItem> adapter = null;



    private static final String STATETAG = "FoodList State Change";
    private static final String DEBUG = "Debug";

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
        setContentView(R.layout.foodlist);


        ArrayList<String> foodList = new ArrayList<String>();
        groupName = getIntent().getExtras().getString("foodGroup").replaceAll("\\s", "").toLowerCase();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Set itemSet = sharedPref.getStringSet(groupName+"_cache", null);
        if(!(itemSet == null))
        {
            grocItems = parseCache(itemSet);
            adapter = new GroceryItemCustomAdapter(this, grocItems);
            ListView foodListView = (ListView)findViewById(R.id.FoodListView);
            foodListView.setAdapter(adapter);
            return;
        }

        try
        {
//            File file = Environment.getExternalStorageDirectory();
//            String path = file.getAbsolutePath();
            InputStream stream = null;
            switch(groupName)
            {
                case "dairy":
                    stream = getResources().openRawResource(R.raw.dairy);
                    break;
                case "fruits&vegetables":
                    stream = getResources().openRawResource(R.raw.fruits_vegetables);
                    break;
                case "other":
                    stream = getResources().openRawResource(R.raw.other);
                    break;
                case "meat":
                    stream = getResources().openRawResource(R.raw.meat);
                    break;
                case "grain":
                    stream = getResources().openRawResource(R.raw.grain);
                    break;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                foodList.add(line);
            }
            reader.close();

        }
        catch(Exception e)
        {
            Toast.makeText(this, "Exception"+ e.getMessage(),Toast.LENGTH_LONG);
        }

        String[] foods = foodList.toArray(new String[foodList.size()]);
        grocItems = new GroceryItem[foods.length];
        for(int i = 0; i < foods.length; i++)
        {
            grocItems[i] = new GroceryItem(foods[i], 0, false, new Date());
        }

        adapter = new GroceryItemCustomAdapter(this, grocItems);
        ListView foodListView = (ListView)findViewById(R.id.FoodListView);
        foodListView.setAdapter(adapter);


    }

    private GroceryItem[] parseCache(Set itemSet)
    {
        Comparator<GroceryItem> comparator = new GroceryItemComparator();
        PriorityQueue<GroceryItem> queue = new PriorityQueue<GroceryItem>(itemSet.size(), comparator);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        Iterator iterator = itemSet.iterator();
        String[] rowsplit = null;
        Date date = null;
        while(iterator.hasNext())
        {
            try
            {
                rowsplit = iterator.next().toString().split(",");
                date = sdf.parse(rowsplit[3]);
                queue.add(new GroceryItem(rowsplit[0],
                        Integer.parseInt(rowsplit[1]), Boolean.parseBoolean(rowsplit[2]), date));
            } catch (ParseException e)
            {
                e.getMessage();
            }

        }

        GroceryItem[] groceryItems  = new GroceryItem[queue.size()];
        for(int i = 0; i < groceryItems.length; i++)
        {
            groceryItems[i] = queue.peek();
            queue.remove(queue.peek());
        }

        return groceryItems;

    }

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

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();

        GroceryItem grocItem;
        Set itemSet = new HashSet();
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        for(int i = 0; i < grocItems.length; i++)
        {
            grocItem = adapter.getItem(adapter.getPosition(grocItems[i]));
            itemSet.add(grocItem.itemName+","+grocItem.qty+","+grocItem.isSelected+","+
                    sdfDate.format(grocItem.lastAccessed));
        }
        editor.putStringSet(groupName+"_cache", itemSet);
        editor.commit();
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
