package com.leon.initialize_gis.di.view_model;

import static com.leon.initialize_gis.helpers.MyApplication.getActivityComponent;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import com.leon.initialize_gis.R;
import com.leon.initialize_gis.activities.MainActivity;
import com.leon.initialize_gis.enums.DialogType;
import com.leon.initialize_gis.utils.custom_dialog.LovelyStandardDialog;

import javax.inject.Inject;

public class DialogModel {
    private final LovelyStandardDialog lovelyStandardDialog;
    private Context context;

    @Inject
    public DialogModel(Context context) {
        lovelyStandardDialog = new LovelyStandardDialog(context);
    }

    public DialogModel(DialogType choose, Context context, String message, String title,
                       String top, String buttonText, Inline... inline) {
        this.context = context;
        lovelyStandardDialog = getActivityComponent().LovelyStandardDialog();
        lovelyStandardDialog.setTitle(title).setMessage(message).setTopTitle(top);
        if (choose == DialogType.Green)
            CustomGreenDialog(buttonText);
        else if (choose == DialogType.Yellow)
            CustomYellowDialog(buttonText);
        else if (choose == DialogType.Red)
            CustomRedDialog(buttonText);
        else if (choose == DialogType.GreenRedirect)
            CustomGreenDialogRedirect(buttonText);
        else if (choose == DialogType.YellowRedirect)
            CustomYellowDialogRedirect(buttonText, inline);
        else if (choose == DialogType.RedRedirect)
            CustomRedDialogRedirect(buttonText);
    }

    public LovelyStandardDialog getLovelyStandardDialog() {
        return lovelyStandardDialog;
    }

    public void CustomGreenDialogRedirect(String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.green)
                .setTopTitleColor(ContextCompat.getColor(context, R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_green_1)
                .setPositiveButton(ButtonText, v -> {
                    Intent intent = new Intent(context, MainActivity.class);
                    context.startActivity(intent);
                });
        lovelyStandardDialog.show();
    }

    public void CustomYellowDialogRedirect(String buttonText, Inline... inlines) {
        lovelyStandardDialog
                .setTopTitleColor(ContextCompat.getColor(context, R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_yellow_1)
                .setTopColorRes(R.color.yellow)
                .setPositiveButton(buttonText, v -> inlines[0].inline())
                .show();
    }

    public void CustomRedDialogRedirect(String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red)
                .setTopTitleColor(ContextCompat.getColor(context, R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_red_1)
                .setPositiveButton(buttonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public void CustomGreenDialog(String ButtonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.green)
                .setTopTitleColor(ContextCompat.getColor(context, R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_green_1)
                .setPositiveButton(ButtonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public void CustomYellowDialog(String buttonText) {
        lovelyStandardDialog
                .setTopTitleColor(ContextCompat.getColor(context, R.color.text_color_light))
                .setTopColorRes(R.color.yellow)
                .setButtonsBackground(R.drawable.border_yellow_1)
                .setPositiveButton(buttonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public void CustomRedDialog(String buttonText) {
        lovelyStandardDialog
                .setTopColorRes(R.color.red)
                .setTopTitleColor(ContextCompat.getColor(context, R.color.text_color_light))
                .setButtonsBackground(R.drawable.border_red_1)
                .setPositiveButton(buttonText, v -> lovelyStandardDialog.dismiss())
                .show();
    }

    public interface Inline {
        void inline();
    }
}
