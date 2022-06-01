package com.leon.initialize_gis.adapters.items;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class DrawerItem {
    private final String ItemName;
    private final Drawable drawable;

    DrawerItem(String itemName, Drawable drawable) {
        this.ItemName = itemName;
        this.drawable = drawable;
    }

    public static ArrayList<DrawerItem> createItemList(String[] menu, TypedArray drawable) {
        final ArrayList<DrawerItem> drawerItems = new ArrayList<>();
        final int length = Math.min(menu.length, drawable.length());
        for (int i = 0; i < length; i++) {
            drawerItems.add(new DrawerItem(menu[i], drawable.getDrawable(i)));
        }
        return drawerItems;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getItemName() {
        return ItemName;
    }
}
