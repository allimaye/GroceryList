package com.hostzi.aniketlimaye.grocerylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String[] foodGroups = {"Dairy", "Fruits & Vegetables", "Grain", "Meat", "Other"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new MainMenuCustomAdapter(this, foodGroups);
        ListView mainListView = (ListView)findViewById(R.id.MainListView);

        mainListView.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml

        switch(item.getItemId())
        {
            case R.id.SendTextOption:
                Intent intent = new Intent(this, SendText.class);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                Set itemSet = null;
                Bundle bundle = new Bundle();
                String groupName = "";


                for(int i = 0; i < foodGroups.length; i++)
                {
                    try
                    {
                        groupName = foodGroups[i].replaceAll("\\s", "").toLowerCase();
                        itemSet = sharedPref.getStringSet(groupName+"_cache", null);
                        //if cache hasn't been created yet, create it.
                        if(itemSet == null)
                        {
                            itemSet = createBlankCache(groupName);
                        }

                        Iterator iterator = itemSet.iterator();
                        int counter = 0;
                        String[] rows = new String[itemSet.size()];
                        while(iterator.hasNext())
                        {
                            rows[counter] = iterator.next().toString();
                            counter++;
                        }
                        bundle.putStringArray(groupName+"_cache", rows);
                    }
                    catch(Exception e)
                    {
                        continue;
                    }
                }
                bundle.putStringArray("foodGroups", foodGroups);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;


            case R.id.ClearInfoOption:
                SharedPreferences sharedPref2 = PreferenceManager.getDefaultSharedPreferences(this);

                for (int i = 0; i < foodGroups.length; i++)
                {
                    try
                    {
                        String groupName2 = foodGroups[i].replaceAll("\\s", "").toLowerCase();
                        Set itemSet2 = sharedPref2.getStringSet(groupName2 + "_cache", null);
                        if(itemSet2 == null)
                        {
                            itemSet2 = createBlankCache(groupName2);
                        }
                        Set replaceSet = new HashSet();
                        Iterator iterator = itemSet2.iterator();
                        while(iterator.hasNext())
                        {
                            String[] rowSplit = iterator.next().toString().split(",");
                            rowSplit[1] = "0";
                            rowSplit[2] = "false";
                            replaceSet.add(rowSplit[0] + "," + rowSplit[1] + "," +rowSplit[2] + "," +
                                    rowSplit[3]);
                        }

                        SharedPreferences.Editor editor = sharedPref2.edit();
                        editor.remove(groupName2 + "_cache");
                        editor.putStringSet(groupName2+ "_cache", replaceSet);
                        editor.commit();
                    }
                    catch(Exception e)
                    {
                        continue;
                    }
                }

                break;



        }


        return super.onOptionsItemSelected(item);
    }

    public Set createBlankCache(String groupName)
    {
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

        Set itemSet = new HashSet();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
        String line = "";
        try
        {
            while ((line = reader.readLine()) != null)
            {
                itemSet.add(line+",0,false,"+sdf.format(new Date()));
            }
            reader.close();
        }
        catch (Exception e){}

        return itemSet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.clear();
//        editor.commit();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}
