package com.leon.initialize_gis.adapters;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;
import static com.leon.initialize_gis.utils.ShowFragmentDialog.ShowDialogOnce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.adapters.holders.UsersPointsViewHolder;
import com.leon.initialize_gis.fragments.RoadMapFragment;
import com.leon.initialize_gis.tables.UsersPoints;

import java.util.ArrayList;
import java.util.Locale;

public class UsersPointsAdapter extends RecyclerView.Adapter<UsersPointsViewHolder> {
    private final ArrayList<UsersPoints> usersPoints, usersPointsTemp;
    private final Context context;

    public UsersPointsAdapter(ArrayList<UsersPoints> usersPoints, Context context) {
        this.usersPointsTemp = new ArrayList<>(usersPoints);
        this.usersPoints = new ArrayList<>(usersPoints);
        this.context = context;
    }

    @NonNull
    @Override
    public UsersPointsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(R.layout.users_points_item, viewGroup, false);
        return new UsersPointsViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull UsersPointsViewHolder usersPointsViewHolder, int i) {
        final UsersPoints userPoint = usersPoints.get(i);
        usersPointsViewHolder.linearLayout.setBackground(ContextCompat.getDrawable(context,
                i % 2 == 0 ? R.drawable.border_gray_1 : R.drawable.border_gray_3));
        usersPointsViewHolder.textViewEshterak.setText(userPoint.eshterak);
        usersPointsViewHolder.textViewDate.setText(userPoint.date);
        usersPointsViewHolder.imageViewDelete.setOnClickListener(view -> {
            getApplicationComponent().MyDatabase().usersPointDao().deleteUserPoint(userPoint.eshterak);
            boolean found = false;
            int j = 0;
            while (!found) {
                if (usersPointsTemp.get(j).eshterak.equals(usersPoints.get(i).eshterak)) {
                    usersPointsTemp.remove(j);
                    found = true;
                }
                j++;
            }
            usersPoints.remove(i);
            notifyDataSetChanged();
        });
        usersPointsViewHolder.imageViewLocation.setOnClickListener(view ->
                ShowDialogOnce(context, "MAP_DIALOG", RoadMapFragment.newInstance(userPoint.x,
                        userPoint.y)));
    }

    @Override
    public int getItemCount() {
        return usersPoints.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        usersPoints.clear();
        if (charText.length() == 0) {
            usersPoints.addAll(usersPointsTemp);
        } else {
            for (UsersPoints userPoint : usersPointsTemp) {
                if (userPoint.eshterak.toLowerCase(Locale.getDefault()).contains(charText)) {
                    usersPoints.add(userPoint);
                }
            }
        }
        notifyDataSetChanged();
    }
}
