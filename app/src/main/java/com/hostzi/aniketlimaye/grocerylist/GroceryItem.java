package com.hostzi.aniketlimaye.grocerylist;

import java.util.Date;

/**
 * Created by Aniket on 29/08/2015.
 */
public class GroceryItem {

    public String itemName;
    public int qty;
    public boolean isSelected;
    public Date lastAccessed;

    public GroceryItem(String itemName, int qty, boolean isSelected, Date date)
    {
        this.itemName = itemName;
        this.qty = qty;
        this.isSelected = isSelected;
        lastAccessed = date;
    }


}
