package com.leon.initialize_gis.di.view_model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.utils.CustomToast;

public final class ProgressModel {
    public static ProgressModel instance = null;
    private Dialog dialog;

    public static ProgressModel getInstance() {
        if (instance == null) {
            instance = new ProgressModel();
        }
        return instance;
    }

    public void show(Context context) {
        show(context, "");
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, false, title);
    }

    public Dialog show(Context context, boolean cancelable, CharSequence title) {
        return show(context, title, cancelable, dialog ->
                new CustomToast().warning(context.getString(R.string.canceled),
                        Toast.LENGTH_LONG));
    }

    public void show(Context context, boolean cancelable) {
        show(context, context.getString(R.string.waiting), cancelable, dialog ->
                new CustomToast().warning(context.getString(R.string.canceled), Toast.LENGTH_LONG));
    }

    @SuppressLint("InflateParams")
    public Dialog show(Context context, CharSequence title, DialogInterface.OnCancelListener cancelListener) {
        return show(context, title, true, cancelListener);
    }

    @SuppressLint("InflateParams")
    public Dialog show(Context context, DialogInterface.OnCancelListener cancelListener) {
        return show(context, context.getString(R.string.waiting), true, cancelListener);
    }

    @SuppressLint("InflateParams")
    public Dialog show(Context context, CharSequence title, boolean cancelable,
                       DialogInterface.OnCancelListener cancelListener) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.progress_bar, null);
        dialog = new Dialog(context, R.style.NewDialog);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        final TextView tv = view.findViewById(R.id.text_view_title);
        tv.setText(title);
        setCancelable(cancelable, view, cancelListener);
        if (!((Activity) context).isFinishing()) {
            try {
                dialog.show();
            } catch (WindowManager.BadTokenException e) {
                e.printStackTrace();
            }
        }
        return dialog;
    }

    private void setCancelable(boolean cancelable, View view, DialogInterface.OnCancelListener cancelListener) {
        dialog.setCancelable(cancelable);
        if (cancelable) {
            dialog.setOnCancelListener(cancelListener);
            RelativeLayout relativeLayout = view.findViewById(R.id.relative_layout);
            relativeLayout.setOnClickListener(v -> cancelDialog());
        }
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void cancelDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog.cancel();
            dialog = null;
        }
    }
}