package com.leon.initialize_gis.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import androidx.core.content.ContextCompat;

import com.leon.initialize_gis.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SpinnerCustomAdapter extends BaseAdapter {
    private final ArrayList<String> items;
    private final LayoutInflater inflater;

    public SpinnerCustomAdapter(Context context, ArrayList<String> items) {
        super();
        this.items = items;
        inflater = (LayoutInflater.from(context));
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_dropdown_menu, null);
        final CheckedTextView item = view.findViewById(R.id.checked_text_view);
        item.setText(items.get(position));
        return view;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getDropDownView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.item_dropdown_popup, null);
        final CheckedTextView item = view.findViewById(R.id.checked_text_view);
        item.setText(items.get(position));
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ArrayAdapter<String> createArrayAdapter(final List<String> arrayListSpinner, Context context) {
        return new ArrayAdapter<String>(context,
                R.layout.item_dropdown_menu_popup, arrayListSpinner) {
            @NotNull
            @Override
            public View getView(int position, View convertView, @NotNull ViewGroup parent) {
                final View view = super.getView(position, convertView, parent);
                final CheckedTextView textView = view.findViewById(android.R.id.text1);
                textView.setChecked(true);
                textView.setTextSize(context.getResources().getDimension(R.dimen.text_size_small));
                textView.setTextColor(ContextCompat.getColor(context, android.R.color.black));
                return view;
            }
        };
    }
}
