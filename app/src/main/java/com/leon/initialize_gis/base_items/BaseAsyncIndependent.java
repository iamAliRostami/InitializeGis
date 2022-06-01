package com.leon.initialize_gis.base_items;

import static com.leon.initialize_gis.helpers.MyApplication.getApplicationComponent;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.leon.initialize_gis.di.view_model.ProgressModel;

public abstract class BaseAsyncIndependent extends AsyncTask<Activity, Void, Void> {
    private final ProgressModel progress;
    private final Object view;
    private final boolean aBoolean;

    public BaseAsyncIndependent(Context context, Object... view) {
        super();
        this.view = view[0];
        progress = getApplicationComponent().ProgressModel();
        progress.show(context);
        if (view.length > 1) {
            this.aBoolean = (Boolean) view[1];
        } else {
            this.aBoolean = true;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        preTask(view);
    }

    @Override
    protected Void doInBackground(Activity... contexts) {
        backgroundTask((Context) contexts[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        postTask(view);
        if (aBoolean)
            progress.cancelDialog();
    }

    public ProgressModel getProgress() {
        return progress;
    }

    public abstract void postTask(Object o);

    public abstract void preTask(Object o);

    public abstract void backgroundTask(Context context);
}
