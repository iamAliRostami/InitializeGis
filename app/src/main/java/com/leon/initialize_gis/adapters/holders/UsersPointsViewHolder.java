package com.leon.initialize_gis.adapters.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.initialize_gis.R;

public class UsersPointsViewHolder extends RecyclerView.ViewHolder {
    public final LinearLayoutCompat linearLayout;
    public final TextView textViewEshterak;
    public final TextView textViewDate;
    public final ImageView imageViewDelete;
    public final ImageView imageViewLocation;

    public UsersPointsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.textViewEshterak = itemView.findViewById(R.id.text_view_eshterak);
        this.textViewDate = itemView.findViewById(R.id.text_view_date);
        this.imageViewDelete = itemView.findViewById(R.id.image_view_delete);
        this.imageViewLocation = itemView.findViewById(R.id.image_view_location);
        this.linearLayout = itemView.findViewById(R.id.linear_layout_users_point);
    }
}
