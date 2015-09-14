package com.hostzi.aniketlimaye.grocerylist;

/**
 * Created by Aniket on 01/09/2015.
 */
public class Tuple<First, Second> {
    public final First first;
    public final Second second;
    public Tuple(First x, Second y) {
        this.first = x;
        this.second = y;
    }
}