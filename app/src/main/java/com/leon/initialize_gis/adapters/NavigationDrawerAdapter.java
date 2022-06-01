package com.leon.initialize_gis.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.adapters.holders.DrawerItemHolder;
import com.leon.initialize_gis.adapters.items.DrawerItem;

import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<DrawerItemHolder> {
    private final List<DrawerItem> drawerItemList;

    public NavigationDrawerAdapter(final List<DrawerItem> listItems) {
        this.drawerItemList = listItems;
    }

    @NonNull
    @Override
    public DrawerItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View drawerView = inflater.inflate(R.layout.item_navigation_drawer, parent, false);
        return new DrawerItemHolder(drawerView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerItemHolder holder, int position) {
        final DrawerItem drawerItem = drawerItemList.get(position);
        holder.imageViewIcon.setImageDrawable(drawerItem.getDrawable());
        holder.textViewTitle.setText(drawerItem.getItemName());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }
}