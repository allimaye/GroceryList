package com.hostzi.aniketlimaye.grocerylist;

import java.util.Comparator;

/**
 * Created by Aniket on 02/09/2015.
 */
public class GroceryItemComparator implements Comparator<GroceryItem> {

    @Override
    public int compare(GroceryItem lhs, GroceryItem rhs) {
        return rhs.lastAccessed.compareTo(lhs.lastAccessed);
        //return lhs.lastAccessed.compareTo(rhs.lastAccessed);
    }
}
