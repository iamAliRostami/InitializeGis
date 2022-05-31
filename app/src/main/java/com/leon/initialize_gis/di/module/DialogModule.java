package com.leon.initialize_gis.di.module;

import android.content.Context;

import com.leon.initialize_gis.di.view_model.DialogModel;
import com.leon.initialize_gis.utils.custom_dialog.LovelyStandardDialog;

import dagger.Module;
import dagger.Provides;

@Module
public class DialogModule {
    private final LovelyStandardDialog lovelyStandardDialog;

    public DialogModule(Context context) {
        DialogModel dialogModel = new DialogModel(context);
        this.lovelyStandardDialog = dialogModel.getLovelyStandardDialog();
    }

    @Provides
    public LovelyStandardDialog provideLovelyStandardDialog() {
        return lovelyStandardDialog;
    }
}
